package sk.richardschleger.posipanion.models;

import java.sql.Timestamp;
import java.util.Objects;

public class WayPointModel {
    
    private double latitude;

    private double longitude;

    private Timestamp timestamp;

    public WayPointModel() {
    }

    public WayPointModel(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public WayPointModel(double latitude, double longitude, Timestamp timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
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

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public WayPointModel latitude(double latitude) {
        setLatitude(latitude);
        return this;
    }

    public WayPointModel longitude(double longitude) {
        setLongitude(longitude);
        return this;
    }

    public WayPointModel timestamp(Timestamp timestamp) {
        setTimestamp(timestamp);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof WayPointModel)) {
            return false;
        }
        WayPointModel wayPointModel = (WayPointModel) o;
        return latitude == wayPointModel.latitude && longitude == wayPointModel.longitude && Objects.equals(timestamp, wayPointModel.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, timestamp);
    }

    @Override
    public String toString() {
        return "{" +
            " latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }

}
