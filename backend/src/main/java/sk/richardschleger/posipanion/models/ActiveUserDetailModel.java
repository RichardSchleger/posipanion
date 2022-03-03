package sk.richardschleger.posipanion.models;

import java.util.Objects;

public class ActiveUserDetailModel {
    
    private int id;

    private String firstName;

    private String surname;

    private double lastKnownLatitude;

    private double lastKnownLongitude;

    private TrackModel track;

    private TrackModel currentRide;

    private int percentage;

    public ActiveUserDetailModel() {
    }

    public ActiveUserDetailModel(int id, String firstName, String surname, double lastKnownLatitude, double lastKnownLongitude, TrackModel track, TrackModel currentRide, int percentage) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.lastKnownLatitude = lastKnownLatitude;
        this.lastKnownLongitude = lastKnownLongitude;
        this.track = track;
        this.currentRide = currentRide;
        this.percentage = percentage;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public double getLastKnownLatitude() {
        return this.lastKnownLatitude;
    }

    public void setLastKnownLatitude(double lastKnownLatitude) {
        this.lastKnownLatitude = lastKnownLatitude;
    }

    public double getLastKnownLongitude() {
        return this.lastKnownLongitude;
    }

    public void setLastKnownLongitude(double lastKnownLongitude) {
        this.lastKnownLongitude = lastKnownLongitude;
    }

    public TrackModel getTrack() {
        return this.track;
    }

    public void setTrack(TrackModel track) {
        this.track = track;
    }

    public TrackModel getCurrentRide() {
        return this.currentRide;
    }

    public void setCurrentRide(TrackModel currentRide) {
        this.currentRide = currentRide;
    }

    public int getPercentage() {
        return this.percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public ActiveUserDetailModel id(int id) {
        setId(id);
        return this;
    }

    public ActiveUserDetailModel firstName(String firstName) {
        setFirstName(firstName);
        return this;
    }

    public ActiveUserDetailModel surname(String surname) {
        setSurname(surname);
        return this;
    }

    public ActiveUserDetailModel lastKnownLatitude(double lastKnownLatitude) {
        setLastKnownLatitude(lastKnownLatitude);
        return this;
    }

    public ActiveUserDetailModel lastKnownLongitude(double lastKnownLongitude) {
        setLastKnownLongitude(lastKnownLongitude);
        return this;
    }

    public ActiveUserDetailModel track(TrackModel track) {
        setTrack(track);
        return this;
    }

    public ActiveUserDetailModel currentRide(TrackModel currentRide) {
        setCurrentRide(currentRide);
        return this;
    }

    public ActiveUserDetailModel percentage(int percentage) {
        setPercentage(percentage);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ActiveUserDetailModel)) {
            return false;
        }
        ActiveUserDetailModel activeUserDetailModel = (ActiveUserDetailModel) o;
        return id == activeUserDetailModel.id && Objects.equals(firstName, activeUserDetailModel.firstName) && Objects.equals(surname, activeUserDetailModel.surname) && lastKnownLatitude == activeUserDetailModel.lastKnownLatitude && lastKnownLongitude == activeUserDetailModel.lastKnownLongitude && Objects.equals(track, activeUserDetailModel.track) && Objects.equals(currentRide, activeUserDetailModel.currentRide) && percentage == activeUserDetailModel.percentage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, surname, lastKnownLatitude, lastKnownLongitude, track, currentRide, percentage);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", lastKnownLatitude='" + getLastKnownLatitude() + "'" +
            ", lastKnownLongitude='" + getLastKnownLongitude() + "'" +
            ", track='" + getTrack() + "'" +
            ", currentRide='" + getCurrentRide() + "'" +
            ", percentage='" + getPercentage() + "'" +
            "}";
    }

}
