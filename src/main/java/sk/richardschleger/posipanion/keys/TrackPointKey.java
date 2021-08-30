package sk.richardschleger.posipanion.keys;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class TrackPointKey {
    
    @PrimaryKeyColumn(name = "trackid", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID trackId;

    @PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private UUID trackPointId;

    public TrackPointKey() {
    }

    public TrackPointKey(UUID trackId, UUID trackPointId) {
        this.trackId = trackId;
        this.trackPointId = trackPointId;
    }

    public UUID getTrackId() {
        return this.trackId;
    }

    public void setTrackId(UUID trackId) {
        this.trackId = trackId;
    }

    public UUID getTrackPointId() {
        return this.trackPointId;
    }

    public void setTrackPointId(UUID trackPointId) {
        this.trackPointId = trackPointId;
    }

    public TrackPointKey trackId(UUID trackId) {
        setTrackId(trackId);
        return this;
    }

    public TrackPointKey trackPointId(UUID trackPointId) {
        setTrackPointId(trackPointId);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TrackPointKey)) {
            return false;
        }
        TrackPointKey trackPointKey = (TrackPointKey) o;
        return Objects.equals(trackId, trackPointKey.trackId) && Objects.equals(trackPointId, trackPointKey.trackPointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackId, trackPointId);
    }

    @Override
    public String toString() {
        return "{" +
            " trackId='" + getTrackId() + "'" +
            ", trackPointId='" + getTrackPointId() + "'" +
            "}";
    }

}
