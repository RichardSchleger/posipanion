package sk.richardschleger.posipanion.entities;

import java.util.Objects;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import sk.richardschleger.posipanion.keys.TrackKey;

@Table
public class Track {
    
    @PrimaryKey
    private TrackKey key;

    private long stravaId;

    private String name;

    private double distance;

    private double elevationGain;

    private long movingTime;

    public Track() {
    }

    public Track(TrackKey key, long stravaId, String name, double distance, double elevationGain, long movingTime) {
        this.key = key;
        this.stravaId = stravaId;
        this.name = name;
        this.distance = distance;
        this.elevationGain = elevationGain;
        this.movingTime = movingTime;
    }

    public TrackKey getKey() {
        return this.key;
    }

    public void setKey(TrackKey key) {
        this.key = key;
    }

    public long getStravaId() {
        return this.stravaId;
    }

    public void setStravaId(long stravaId) {
        this.stravaId = stravaId;
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

    public double getElevationGain() {
        return this.elevationGain;
    }

    public void setElevationGain(double elevationGain) {
        this.elevationGain = elevationGain;
    }

    public long getMovingTime() {
        return this.movingTime;
    }

    public void setMovingTime(long movingTime) {
        this.movingTime = movingTime;
    }

    public Track key(TrackKey key) {
        setKey(key);
        return this;
    }

    public Track stravaId(long stravaId) {
        setStravaId(stravaId);
        return this;
    }

    public Track name(String name) {
        setName(name);
        return this;
    }

    public Track distance(double distance) {
        setDistance(distance);
        return this;
    }

    public Track elevationGain(double elevationGain) {
        setElevationGain(elevationGain);
        return this;
    }

    public Track movingTime(long movingTime) {
        setMovingTime(movingTime);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Track)) {
            return false;
        }
        Track track = (Track) o;
        return Objects.equals(key, track.key) && stravaId == track.stravaId && Objects.equals(name, track.name) && distance == track.distance && elevationGain == track.elevationGain && movingTime == track.movingTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, stravaId, name, distance, elevationGain, movingTime);
    }

    @Override
    public String toString() {
        return "{" +
            " key='" + getKey() + "'" +
            ", stravaId='" + getStravaId() + "'" +
            ", name='" + getName() + "'" +
            ", distance='" + getDistance() + "'" +
            ", elevationGain='" + getElevationGain() + "'" +
            ", movingTime='" + getMovingTime() + "'" +
            "}";
    }

}
