package sk.richardschleger.posipanion.models;

import java.util.Objects;

public class FriendModel {
    
    private int id;

    private int friendId;

    private String firstName;

    private String surname;

    private double lastKnownLatitude;

    private double lastKnownLongitude;

    private boolean canConfirm;

    public FriendModel() {
    }

    public FriendModel(int id, int friendId, String firstName, String surname, double lastKnownLatitude, double lastKnownLongitude, boolean canConfirm) {
        this.id = id;
        this.friendId = friendId;
        this.firstName = firstName;
        this.surname = surname;
        this.lastKnownLatitude = lastKnownLatitude;
        this.lastKnownLongitude = lastKnownLongitude;
        this.canConfirm = canConfirm;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFriendId() {
        return this.friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
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

    public boolean isCanConfirm() {
        return this.canConfirm;
    }

    public boolean getCanConfirm() {
        return this.canConfirm;
    }

    public void setCanConfirm(boolean canConfirm) {
        this.canConfirm = canConfirm;
    }

    public FriendModel id(int id) {
        setId(id);
        return this;
    }

    public FriendModel friendId(int friendId) {
        setFriendId(friendId);
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

    public FriendModel canConfirm(boolean canConfirm) {
        setCanConfirm(canConfirm);
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
        return id == friendModel.id && friendId == friendModel.friendId && Objects.equals(firstName, friendModel.firstName) && Objects.equals(surname, friendModel.surname) && lastKnownLatitude == friendModel.lastKnownLatitude && lastKnownLongitude == friendModel.lastKnownLongitude && canConfirm == friendModel.canConfirm;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, friendId, firstName, surname, lastKnownLatitude, lastKnownLongitude, canConfirm);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", friendId='" + getFriendId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", lastKnownLatitude='" + getLastKnownLatitude() + "'" +
            ", lastKnownLongitude='" + getLastKnownLongitude() + "'" +
            ", canConfirm='" + isCanConfirm() + "'" +
            "}";
    }

}
