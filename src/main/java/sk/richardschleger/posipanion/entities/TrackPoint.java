package sk.richardschleger.posipanion.entities;

import java.util.Objects;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import sk.richardschleger.posipanion.keys.TrackPointKey;

@Table
public class TrackPoint {

    @PrimaryKey
    private TrackPointKey key;

    private double latitude;

    private double longitude;

    private double elevation;

    private int order;

    public TrackPoint() {
    }

    public TrackPoint(TrackPointKey key, double latitude, double longitude, double elevation, int order) {
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.order = order;
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

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public TrackPoint key(TrackPointKey key) {
        setKey(key);
        return this;
    }

    public TrackPoint latitude(double latitude) {
        setLatitude(latitude);
        return this;
    }

    public TrackPoint longitude(double longitude) {
        setLongitude(longitude);
        return this;
    }

    public TrackPoint elevation(double elevation) {
        setElevation(elevation);
        return this;
    }

    public TrackPoint order(int order) {
        setOrder(order);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TrackPoint)) {
            return false;
        }
        TrackPoint trackPoint = (TrackPoint) o;
        return Objects.equals(key, trackPoint.key) && latitude == trackPoint.latitude && longitude == trackPoint.longitude && elevation == trackPoint.elevation && order == trackPoint.order;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, latitude, longitude, elevation, order);
    }

    @Override
    public String toString() {
        return "{" +
            " key='" + getKey() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", elevation='" + getElevation() + "'" +
            ", order='" + getOrder() + "'" +
            "}";
    }

}
