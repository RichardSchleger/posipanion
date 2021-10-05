package sk.richardschleger.posipanion.models;

import java.util.Objects;

public class ProfileModel {
    
    private String firstName;

    private String surname;

    private long stravaId;

    private boolean stravaUploadActivity;

    public ProfileModel() {
    }

    public ProfileModel(String firstName, String surname, long stravaId, boolean stravaUploadActivity) {
        this.firstName = firstName;
        this.surname = surname;
        this.stravaId = stravaId;
        this.stravaUploadActivity = stravaUploadActivity;
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

    public long getStravaId() {
        return this.stravaId;
    }

    public void setStravaId(long stravaId) {
        this.stravaId = stravaId;
    }

    public boolean isStravaUploadActivity() {
        return this.stravaUploadActivity;
    }

    public boolean getStravaUploadActivity() {
        return this.stravaUploadActivity;
    }

    public void setStravaUploadActivity(boolean stravaUploadActivity) {
        this.stravaUploadActivity = stravaUploadActivity;
    }

    public ProfileModel firstName(String firstName) {
        setFirstName(firstName);
        return this;
    }

    public ProfileModel surname(String surname) {
        setSurname(surname);
        return this;
    }

    public ProfileModel stravaId(long stravaId) {
        setStravaId(stravaId);
        return this;
    }

    public ProfileModel stravaUploadActivity(boolean stravaUploadActivity) {
        setStravaUploadActivity(stravaUploadActivity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProfileModel)) {
            return false;
        }
        ProfileModel profileModel = (ProfileModel) o;
        return Objects.equals(firstName, profileModel.firstName) && Objects.equals(surname, profileModel.surname) && stravaId == profileModel.stravaId && stravaUploadActivity == profileModel.stravaUploadActivity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, surname, stravaId, stravaUploadActivity);
    }

    @Override
    public String toString() {
        return "{" +
            " firstName='" + getFirstName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", stravaId='" + getStravaId() + "'" +
            ", stravaUploadActivity='" + isStravaUploadActivity() + "'" +
            "}";
    }

}
