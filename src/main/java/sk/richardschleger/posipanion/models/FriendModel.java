package sk.richardschleger.posipanion.models;

import java.util.Objects;

public class FriendModel {
    
    private int id;

    private String firstName;

    private String surname;

    private double lastKnownLatitude;

    private double lastKnownLongitude;

    public FriendModel() {
    }

    public FriendModel(int id, String firstName, String surname, double lastKnownLatitude, double lastKnownLongitude) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.lastKnownLatitude = lastKnownLatitude;
        this.lastKnownLongitude = lastKnownLongitude;
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

    public FriendModel id(int id) {
        setId(id);
        return this;
    }

    public FriendModel firstName(String firstName) {
        setFirstName(firstName);
        return this;
    }

    public FriendModel surname(String surname) {
        setSurname(surname);
        return this;
    }

    public FriendModel lastKnownLatitude(double lastKnownLatitude) {
        setLastKnownLatitude(lastKnownLatitude);
        return this;
    }

    public FriendModel lastKnownLongitude(double lastKnownLongitude) {
        setLastKnownLongitude(lastKnownLongitude);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FriendModel)) {
            return false;
        }
        FriendModel friendModel = (FriendModel) o;
        return id == friendModel.id && Objects.equals(firstName, friendModel.firstName) && Objects.equals(surname, friendModel.surname) && lastKnownLatitude == friendModel.lastKnownLatitude && lastKnownLongitude == friendModel.lastKnownLongitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, surname, lastKnownLatitude, lastKnownLongitude);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", lastKnownLatitude='" + getLastKnownLatitude() + "'" +
            ", lastKnownLongitude='" + getLastKnownLongitude() + "'" +
            "}";
    }

}
