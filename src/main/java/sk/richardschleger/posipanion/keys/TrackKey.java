package sk.richardschleger.posipanion.keys;

import java.util.Objects;
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

    public TrackKey() {
    }

    public TrackKey(String userEmail, UUID trackId) {
        this.userEmail = userEmail;
        this.trackId = trackId;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public UUID getTrackId() {
        return this.trackId;
    }

    public void setTrackId(UUID trackId) {
        this.trackId = trackId;
    }

    public TrackKey userEmail(String userEmail) {
        setUserEmail(userEmail);
        return this;
    }

    public TrackKey trackId(UUID trackId) {
        setTrackId(trackId);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TrackKey)) {
            return false;
        }
        TrackKey trackKey = (TrackKey) o;
        return Objects.equals(userEmail, trackKey.userEmail) && Objects.equals(trackId, trackKey.trackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, trackId);
    }

    @Override
    public String toString() {
        return "{" +
            " userEmail='" + getUserEmail() + "'" +
            ", trackId='" + getTrackId() + "'" +
            "}";
    }

}
