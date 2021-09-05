package sk.richardschleger.posipanion.keys;

import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class TrackKey {
    
    @PrimaryKeyColumn(name = "useremail", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String userEmail;

    @PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID trackId;
}
