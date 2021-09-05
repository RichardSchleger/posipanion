package sk.richardschleger.posipanion.models;

import java.util.Objects;

public class LocationModel {

    private double latitude;

    private double longitude;

    private double elevation;

    public LocationModel() {
    }

    public LocationModel(double latitude, double longitude, double elevation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
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

    public LocationModel latitude(double latitude) {
        setLatitude(latitude);
        return this;
    }

    public LocationModel longitude(double longitude) {
        setLongitude(longitude);
        return this;
    }

    public LocationModel elevation(double elevation) {
        setElevation(elevation);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof LocationModel)) {
            return false;
        }
        LocationModel locationModel = (LocationModel) o;
        return latitude == locationModel.latitude && longitude == locationModel.longitude && elevation == locationModel.elevation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, elevation);
    }

    @Override
    public String toString() {
        return "{" +
            " latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", elevation='" + getElevation() + "'" +
            "}";
    }
    
}
