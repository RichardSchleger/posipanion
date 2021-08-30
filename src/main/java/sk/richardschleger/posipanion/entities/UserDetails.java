package sk.richardschleger.posipanion.entities;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class UserDetails {
    
    @PrimaryKey
    private UUID id;

    private UUID userId;

    private String firstName;

    private String surname;

    private String stravaAccessToken;

    private long stravaAccessTokenExpiration;

    private String stravaRefreshToken;

    private boolean stravaUploadActivity;

    private Set<String> fcmTokens;

    public UserDetails() {
    }

    public UserDetails(UUID id, UUID userId, String firstName, String surname, String stravaAccessToken, long stravaAccessTokenExpiration, String stravaRefreshToken, boolean stravaUploadActivity, Set<String> fcmTokens) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.surname = surname;
        this.stravaAccessToken = stravaAccessToken;
        this.stravaAccessTokenExpiration = stravaAccessTokenExpiration;
        this.stravaRefreshToken = stravaRefreshToken;
        this.stravaUploadActivity = stravaUploadActivity;
        this.fcmTokens = fcmTokens;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public String getStravaAccessToken() {
        return this.stravaAccessToken;
    }

    public void setStravaAccessToken(String stravaAccessToken) {
        this.stravaAccessToken = stravaAccessToken;
    }

    public long getStravaAccessTokenExpiration() {
        return this.stravaAccessTokenExpiration;
    }

    public void setStravaAccessTokenExpiration(long stravaAccessTokenExpiration) {
        this.stravaAccessTokenExpiration = stravaAccessTokenExpiration;
    }

    public String getStravaRefreshToken() {
        return this.stravaRefreshToken;
    }

    public void setStravaRefreshToken(String stravaRefreshToken) {
        this.stravaRefreshToken = stravaRefreshToken;
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

    public Set<String> getFcmTokens() {
        return this.fcmTokens;
    }

    public void setFcmTokens(Set<String> fcmTokens) {
        this.fcmTokens = fcmTokens;
    }

    public UserDetails id(UUID id) {
        setId(id);
        return this;
    }

    public UserDetails userId(UUID userId) {
        setUserId(userId);
        return this;
    }

    public UserDetails firstName(String firstName) {
        setFirstName(firstName);
        return this;
    }

    public UserDetails surname(String surname) {
        setSurname(surname);
        return this;
    }

    public UserDetails stravaAccessToken(String stravaAccessToken) {
        setStravaAccessToken(stravaAccessToken);
        return this;
    }

    public UserDetails stravaAccessTokenExpiration(long stravaAccessTokenExpiration) {
        setStravaAccessTokenExpiration(stravaAccessTokenExpiration);
        return this;
    }

    public UserDetails stravaRefreshToken(String stravaRefreshToken) {
        setStravaRefreshToken(stravaRefreshToken);
        return this;
    }

    public UserDetails stravaUploadActivity(boolean stravaUploadActivity) {
        setStravaUploadActivity(stravaUploadActivity);
        return this;
    }

    public UserDetails fcmTokens(Set<String> fcmTokens) {
        setFcmTokens(fcmTokens);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserDetails)) {
            return false;
        }
        UserDetails userDetails = (UserDetails) o;
        return Objects.equals(id, userDetails.id) && Objects.equals(userId, userDetails.userId) && Objects.equals(firstName, userDetails.firstName) && Objects.equals(surname, userDetails.surname) && Objects.equals(stravaAccessToken, userDetails.stravaAccessToken) && stravaAccessTokenExpiration == userDetails.stravaAccessTokenExpiration && Objects.equals(stravaRefreshToken, userDetails.stravaRefreshToken) && stravaUploadActivity == userDetails.stravaUploadActivity && Objects.equals(fcmTokens, userDetails.fcmTokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, firstName, surname, stravaAccessToken, stravaAccessTokenExpiration, stravaRefreshToken, stravaUploadActivity, fcmTokens);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", stravaAccessToken='" + getStravaAccessToken() + "'" +
            ", stravaAccessTokenExpiration='" + getStravaAccessTokenExpiration() + "'" +
            ", stravaRefreshToken='" + getStravaRefreshToken() + "'" +
            ", stravaUploadActivity='" + isStravaUploadActivity() + "'" +
            ", fcmTokens='" + getFcmTokens() + "'" +
            "}";
    }

}
