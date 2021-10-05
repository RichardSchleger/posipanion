package sk.richardschleger.posipanion.entities;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tracks")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private int id;

    @Column(name = "strava_id")
    private long stravaId;

    private String name;

    private double distance;

    @Column(name = "elevation_gain")
    private double elevationGain;

    @Column(name = "estimated_moving_time")
    private long estimatedMovingTime;

    @Column(name = "gpx_path")
    private String gpxPath;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    public Track() {
    }

    public Track(int id, long stravaId, String name, double distance, double elevationGain, long estimatedMovingTime, String gpxPath, User user) {
        this.id = id;
        this.stravaId = stravaId;
        this.name = name;
        this.distance = distance;
        this.elevationGain = elevationGain;
        this.estimatedMovingTime = estimatedMovingTime;
        this.gpxPath = gpxPath;
        this.user = user;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getEstimatedMovingTime() {
        return this.estimatedMovingTime;
    }

    public void setEstimatedMovingTime(long estimatedMovingTime) {
        this.estimatedMovingTime = estimatedMovingTime;
    }

    public String getGpxPath() {
        return this.gpxPath;
    }

    public void setGpxPath(String gpxPath) {
        this.gpxPath = gpxPath;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Track id(int id) {
        setId(id);
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

    public Track estimatedMovingTime(long estimatedMovingTime) {
        setEstimatedMovingTime(estimatedMovingTime);
        return this;
    }

    public Track gpxPath(String gpxPath) {
        setGpxPath(gpxPath);
        return this;
    }

    public Track user(User user) {
        setUser(user);
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
        return id == track.id && stravaId == track.stravaId && Objects.equals(name, track.name) && distance == track.distance && elevationGain == track.elevationGain && estimatedMovingTime == track.estimatedMovingTime && Objects.equals(gpxPath, track.gpxPath) && Objects.equals(user, track.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stravaId, name, distance, elevationGain, estimatedMovingTime, gpxPath, user);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", stravaId='" + getStravaId() + "'" +
            ", name='" + getName() + "'" +
            ", distance='" + getDistance() + "'" +
            ", elevationGain='" + getElevationGain() + "'" +
            ", estimatedMovingTime='" + getEstimatedMovingTime() + "'" +
            ", gpxPath='" + getGpxPath() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }

}
