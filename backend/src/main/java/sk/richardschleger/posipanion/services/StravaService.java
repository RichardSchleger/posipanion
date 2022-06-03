package sk.richardschleger.posipanion.services;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import sk.richardschleger.posipanion.entities.StravaUser;
import sk.richardschleger.posipanion.models.StravaRefreshTokenModel;
import sk.richardschleger.posipanion.models.StravaRouteModel;
import sk.richardschleger.posipanion.models.StravaUploadModel;

@Service
public class StravaService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ObjectMapper mapper = new ObjectMapper();

    private final StravaUserService stravaUserService;

    public StravaService(StravaUserService stravaUserService){
        this.stravaUserService = stravaUserService;
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }
    
    public String fetchNewStravaTokens(StravaUser stravaUser){
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://www.strava.com/oauth/token");

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("client_id", "68481"));
            params.add(new BasicNameValuePair("client_secret", "STRAVA_CLIENT_SECRET"));
            params.add(new BasicNameValuePair("grant_type", "refresh_token"));
            params.add(new BasicNameValuePair("refresh_token", stravaUser.getStravaRefreshToken()));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = client.execute(httpPost);

            String responseString = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
            
            try{
                StravaRefreshTokenModel model = mapper.readValue(responseString, StravaRefreshTokenModel.class);
                stravaUser.setStravaAccessToken(model.getAccess_token());
                stravaUser.setStravaAccessTokenExpiration(new Timestamp(model.getExpires_at()));
                stravaUser.setStravaRefreshToken(model.getRefresh_token());

                stravaUserService.saveStravaUser(stravaUser);
            }catch (UnrecognizedPropertyException e){
                logger.info("Error parsing response {}", e.getMessage());
                e.printStackTrace();
            }

            client.close();
        } catch (IOException e) {
            logger.info("Error getting token {}", e.getMessage());
            e.printStackTrace();
        }    
        logger.info("New Strava tokens retrieved for user {}", stravaUser.getUser().getEmail());
        return stravaUser.getStravaAccessToken();
    }

    public void uploadActivityToStrava(StravaUser stravaUser, File gpxFile){

        if(stravaUser.getStravaAccessTokenExpiration().before(new Timestamp(System.currentTimeMillis()))){
            fetchNewStravaTokens(stravaUser);
        }

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://www.strava.com/api/v3/uploads");

            HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("file", new FileBody(gpxFile))
                .addPart("activity_type", new StringBody("ride", ContentType.TEXT_PLAIN))
                .addPart("data_type", new StringBody("gpx", ContentType.TEXT_PLAIN))
                .addPart("external_id", new StringBody(gpxFile.getName(), ContentType.TEXT_PLAIN))
                .addPart("access_token", new StringBody(stravaUser.getStravaAccessToken(), ContentType.TEXT_PLAIN))
                .build();
            
            httpPost.setEntity(entity);
            CloseableHttpResponse response = client.execute(httpPost);

            String responseString = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

            StravaUploadModel model = mapper.readValue(responseString, StravaUploadModel.class);
            logger.info("Uploading activity {} to Strava started with id {}", gpxFile.getName(), model.getId());
            client.close();
        } catch (IOException e) {
            logger.info("Error uploading activity {} to Strava", gpxFile.getName());
            e.printStackTrace();
        }
    }

    public List<StravaRouteModel> getListOfStravaRoutesForUser(StravaUser stravaUser){
        if(stravaUser.getStravaAccessTokenExpiration().before(new Timestamp(System.currentTimeMillis()))){
            fetchNewStravaTokens(stravaUser);
        }

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://www.strava.com/api/v3/athletes/" + stravaUser.getStravaId() + "/routes");

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("page", "1"));
            params.add(new BasicNameValuePair("per_page", "1000"));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            httpPost.setHeader("Authorization", "Bearer " + stravaUser.getStravaAccessToken());

            CloseableHttpResponse response = client.execute(httpPost);

            String responseString = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
            
            List<StravaRouteModel> routes = mapper.readValue(responseString, new TypeReference<List<StravaRouteModel>>(){});

            logger.info("Retrieved {} routes for user {} from Strava", routes.size(), stravaUser.getUser().getEmail());

            client.close();

            return routes;

        } catch (IOException e) {
            logger.info("Error getting list of routes from Strava");
            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    public void deauthorizeStrava(StravaUser stravaUser){
        if(stravaUser.getStravaAccessTokenExpiration().before(new Timestamp(System.currentTimeMillis()))){
            fetchNewStravaTokens(stravaUser);
        }

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://www.strava.com/oauth/deauthorize");

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("access_token", stravaUser.getStravaAccessToken()));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            client.execute(httpPost);

            logger.info("Deauthorized Strava for user {}", stravaUser.getUser().getEmail());

            client.close();

        } catch (IOException e) {
            logger.info("Error deauthorizing Strava for user {}", stravaUser.getUser().getEmail());
            e.printStackTrace();
        }

    }
}
