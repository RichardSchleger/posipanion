package sk.richardschleger.posipanion.models;

import java.util.List;
import java.util.Objects;

public class TrackModel {
    
    private double distance;

    private double elevation;

    private long movingTime;

    private List<WayPointModel> waypoints;

    public TrackModel() {
    }

    public TrackModel(double distance, double elevation, long movingTime, List<WayPointModel> waypoints) {
        this.distance = distance;
        this.elevation = elevation;
        this.movingTime = movingTime;
        this.waypoints = waypoints;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public long getMovingTime() {
        return this.movingTime;
    }

    public void setMovingTime(long movingTime) {
        this.movingTime = movingTime;
    }

    public List<WayPointModel> getWaypoints() {
        return this.waypoints;
    }

    public void setWaypoints(List<WayPointModel> waypoints) {
        this.waypoints = waypoints;
    }

    public TrackModel distance(double distance) {
        setDistance(distance);
        return this;
    }

    public TrackModel elevation(double elevation) {
        setElevation(elevation);
        return this;
    }

    public TrackModel movingTime(long movingTime) {
        setMovingTime(movingTime);
        return this;
    }

    public TrackModel waypoints(List<WayPointModel> waypoints) {
        setWaypoints(waypoints);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TrackModel)) {
            return false;
        }
        TrackModel trackModel = (TrackModel) o;
        return distance == trackModel.distance && elevation == trackModel.elevation && movingTime == trackModel.movingTime && Objects.equals(waypoints, trackModel.waypoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, elevation, movingTime, waypoints);
    }

    @Override
    public String toString() {
        return "{" +
            " distance='" + getDistance() + "'" +
            ", elevation='" + getElevation() + "'" +
            ", movingTime='" + getMovingTime() + "'" +
            ", waypoints='" + getWaypoints() + "'" +
            "}";
    }

}
