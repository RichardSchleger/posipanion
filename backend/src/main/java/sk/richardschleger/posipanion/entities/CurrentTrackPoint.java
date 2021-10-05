package sk.richardschleger.posipanion.entities;

import java.util.Objects;

public class CurrentTrackPoint {

    private int userId;

    private double latitude;

    private double longitude;

    private double elevation;

    private long timestamp;

    public CurrentTrackPoint() {
    }

    public CurrentTrackPoint(int userId, double latitude, double longitude, double elevation, long timestamp) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.timestamp = timestamp;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public CurrentTrackPoint userId(int userId) {
        setUserId(userId);
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
        return userId == currentTrackPoint.userId && latitude == currentTrackPoint.latitude && longitude == currentTrackPoint.longitude && elevation == currentTrackPoint.elevation && timestamp == currentTrackPoint.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, latitude, longitude, elevation, timestamp);
    }

    @Override
    public String toString() {
        return "{" +
            " userId='" + getUserId() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", elevation='" + getElevation() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }

}
