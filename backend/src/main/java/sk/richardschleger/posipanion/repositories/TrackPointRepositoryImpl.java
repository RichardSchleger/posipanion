package sk.richardschleger.posipanion.repositories;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.DeletePredicateRequest;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import sk.richardschleger.posipanion.entities.CurrentTrackPoint;

@Repository
@PropertySource("influx2.properties")
public class TrackPointRepositoryImpl implements TrackPointRepository{

    private InfluxDBClient client;

    private WriteApi writeApi;
    private DeleteApi deleteApi;
    private QueryApi queryApi;

    @Value("${influx2.bucket}")
    private String bucket;

    @Value("${influx2.org}")
    private String org;


    public TrackPointRepositoryImpl() {
        this.client = InfluxDBClientFactory.create();
        this.writeApi = client.makeWriteApi();
        this.deleteApi = client.getDeleteApi();
        this.queryApi = client.getQueryApi();
    }

    @Override
    public List<CurrentTrackPoint> findByUserId(int userId) {
        
        List<CurrentTrackPoint> trackPointList = new ArrayList<>();

        String flux = "from(bucket:\"" + bucket + "\") " + 
            "|> range(start: -1mo) " + 
            "|> filter(fn: (r) => " + 
            "r._measurement == \"trackpoints\" and " +
            "r.userId == \"" + userId + "\")" +
            "|> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";

        

        List<FluxTable> tables = queryApi.query(flux);
        for (FluxTable table : tables) {
            List<FluxRecord> records = table.getRecords();
            for (FluxRecord record : records) {
                CurrentTrackPoint trackPoint = new CurrentTrackPoint();
                if(record.getValueByKey("lat") != null){
                    trackPoint.setLatitude(Double.parseDouble(record.getValueByKey("lat").toString()));
                }
                if(record.getValueByKey("lon") != null){
                    trackPoint.setLongitude(Double.parseDouble(record.getValueByKey("lon").toString()));
                }
                if(record.getValueByKey("ele") != null){
                    trackPoint.setElevation(Double.parseDouble(record.getValueByKey("ele").toString()));
                }
                trackPoint.setTimestamp(record.getTime().toEpochMilli());

                trackPointList.add(trackPoint);
            }
        }

        return trackPointList;
    }

    @Override
    public CurrentTrackPoint findLastByUserId(int userId) {
        String flux = "from(bucket:\"" + bucket + "\") " + 
            "|> range(start: -1mo) " + 
            "|> filter(fn: (r) => " + 
            "r._measurement == \"trackpoints\" and " +
            "r.userId == \"" + userId + "\")" +
            "|> last()" +
            "|> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";

        List<FluxTable> tables = queryApi.query(flux);
        for (FluxTable table : tables) {
            List<FluxRecord> records = table.getRecords();
            for (FluxRecord record : records) {
                CurrentTrackPoint trackPoint = new CurrentTrackPoint();
                if(record.getValueByKey("lat") != null){
                    trackPoint.setLatitude(Double.parseDouble(record.getValueByKey("lat").toString()));
                }
                if(record.getValueByKey("lon") != null){
                    trackPoint.setLongitude(Double.parseDouble(record.getValueByKey("lon").toString()));
                }
                if(record.getValueByKey("ele") != null){
                    trackPoint.setElevation(Double.parseDouble(record.getValueByKey("ele").toString()));
                }
                trackPoint.setTimestamp(record.getTime().toEpochMilli());

                return trackPoint;
            }
        }

        return null;
    }

    @Override
    public void deleteByUserId(int userId) {

        DeletePredicateRequest request = new DeletePredicateRequest();
        request.setPredicate("\"_measurement\" = \"trackpoints\" and \"userId\" = \"" + userId + "\"");
        request.setStart(OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.ofHours(0)));
        request.setStop(OffsetDateTime.now());

        deleteApi.delete(request, bucket, org);

    }

    @Override
    public void save(CurrentTrackPoint trackPoint) {
        
        Point point = Point
        .measurement("trackpoints")
        .addTag("userId", String.valueOf(trackPoint.getUserId()))
        .addField("lat", String.valueOf(trackPoint.getLatitude()))
        .addField("lon", String.valueOf(trackPoint.getLongitude()))
        .addField("ele", String.valueOf(trackPoint.getElevation()))
        .time(Instant.ofEpochMilli(trackPoint.getTimestamp()), WritePrecision.MS);

        writeApi.writePoint(point);
        
    }
    
}
