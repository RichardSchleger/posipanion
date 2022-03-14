package sk.richardschleger.posipanion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.richardschleger.posipanion.entities.User;
import sk.richardschleger.posipanion.entities.UserDetails;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;


@Service
public class GoogleAuthenticateService {

    @Autowired
    UserService userService;
    
    @Autowired
    UserDetailsService userDetailsService;

    public User authenticate(String idTokenString){

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
        // Specify the CLIENT_ID of the app that accesses the backend:
        .setAudience(Collections.singletonList("399407603065-k1d2poogobr5o212kv260vkbmn27p61l.apps.googleusercontent.com"))
        // Or, if multiple clients access the backend:
        //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
        .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                Payload payload = idToken.getPayload();
                
                // Print user identifier
                String userId = payload.getSubject();
                System.out.println("User ID: " + userId);
    
                // // Get profile information from payload
                // String email = payload.getEmail();
                // boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                // String name = (String) payload.get("name");
                // String pictureUrl = (String) payload.get("picture");
                // String locale = (String) payload.get("locale");
                // String familyName = (String) payload.get("family_name");
                // String givenName = (String) payload.get("given_name");
    
                User user = userService.getUserByEmail(payload.getEmail());
                if(user == null){
                    user = new User();
                    user.setEmail(payload.getEmail());
                    user.setPassword("");

                    UserDetails userDetails = new UserDetails();
                    userDetails.setFirstName((String) payload.get("given_name"));
                    userDetails.setSurname((String) payload.get("family_name"));
                    userDetails.setUser(user);
    
                    userDetailsService.saveUserDetails(userDetails);
                }
                return user;
            } else {
                return null;
            }
        } catch (GeneralSecurityException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

}
