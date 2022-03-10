package sk.richardschleger.posipanion.models;

import java.util.List;
import java.util.Objects;

public class TrackModel {

    private int id;
    
    private String name;

    private double distance;

    private double elevation;

    private long movingTime;

    private List<WayPointModel> waypoints;

    public TrackModel() {
    }

    public TrackModel(int id, String name, double distance, double elevation, long movingTime, List<WayPointModel> waypoints) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.elevation = elevation;
        this.movingTime = movingTime;
        this.waypoints = waypoints;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public TrackModel id(int id) {
        setId(id);
        return this;
    }

    public TrackModel name(String name) {
        setName(name);
        return this;
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
        return id == trackModel.id && Objects.equals(name, trackModel.name) && distance == trackModel.distance && elevation == trackModel.elevation && movingTime == trackModel.movingTime && Objects.equals(waypoints, trackModel.waypoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, distance, elevation, movingTime, waypoints);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", distance='" + getDistance() + "'" +
            ", elevation='" + getElevation() + "'" +
            ", movingTime='" + getMovingTime() + "'" +
            ", waypoints='" + getWaypoints() + "'" +
            "}";
    }

}
