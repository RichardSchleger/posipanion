package sk.richardschleger.posipanion.keys;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class TrackPointKey {
    
    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int userId;

    @PrimaryKeyColumn(name = "ord", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private int order;

}
