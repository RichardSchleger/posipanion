package sk.richardschleger.posipanion.entities;

import java.util.Objects;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import sk.richardschleger.posipanion.keys.TrackPointKey;

@Table
public class CurrentTrackPoint {

    @PrimaryKey
    private TrackPointKey key;

    private double latitude;

    private double longitude;

    private double elevation;

    private long timestamp;

    public CurrentTrackPoint() {
    }

    public CurrentTrackPoint(TrackPointKey key, double latitude, double longitude, double elevation, long timestamp) {
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.timestamp = timestamp;
    }

    public TrackPointKey getKey() {
        return this.key;
    }

    public void setKey(TrackPointKey key) {
        this.key = key;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public CurrentTrackPoint key(TrackPointKey key) {
        setKey(key);
        return this;
    }

    public CurrentTrackPoint latitude(double latitude) {
        setLatitude(latitude);
        return this;
    }

    public CurrentTrackPoint longitude(double longitude) {
        setLongitude(longitude);
        return this;
    }

    public CurrentTrackPoint elevation(double elevation) {
        setElevation(elevation);
        return this;
    }

    public CurrentTrackPoint timestamp(long timestamp) {
        setTimestamp(timestamp);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CurrentTrackPoint)) {
            return false;
        }
        CurrentTrackPoint currentTrackPoint = (CurrentTrackPoint) o;
        return Objects.equals(key, currentTrackPoint.key) && latitude == currentTrackPoint.latitude && longitude == currentTrackPoint.longitude && elevation == currentTrackPoint.elevation && timestamp == currentTrackPoint.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, latitude, longitude, elevation, timestamp);
    }

    @Override
    public String toString() {
        return "{" +
            " key='" + getKey() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", elevation='" + getElevation() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }

}
