package sk.richardschleger.posipanion.entities;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class UserStrava {
    
    @PrimaryKey
    private UUID id;

    private String userEmail;

    private long stravaId;

    private String stravaAccessToken;

    private long stravaAccessTokenExpiration;

    private String stravaRefreshToken;

    private boolean stravaUploadActivity;

    public UserStrava() {
    }

    public UserStrava(UUID id, String userEmail, long stravaId, String stravaAccessToken, long stravaAccessTokenExpiration, String stravaRefreshToken, boolean stravaUploadActivity) {
        this.id = id;
        this.userEmail = userEmail;
        this.stravaId = stravaId;
        this.stravaAccessToken = stravaAccessToken;
        this.stravaAccessTokenExpiration = stravaAccessTokenExpiration;
        this.stravaRefreshToken = stravaRefreshToken;
        this.stravaUploadActivity = stravaUploadActivity;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public long getStravaId() {
        return this.stravaId;
    }

    public void setStravaId(long stravaId) {
        this.stravaId = stravaId;
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

    public UserStrava id(UUID id) {
        setId(id);
        return this;
    }

    public UserStrava userEmail(String userEmail) {
        setUserEmail(userEmail);
        return this;
    }

    public UserStrava stravaId(long stravaId) {
        setStravaId(stravaId);
        return this;
    }

    public UserStrava stravaAccessToken(String stravaAccessToken) {
        setStravaAccessToken(stravaAccessToken);
        return this;
    }

    public UserStrava stravaAccessTokenExpiration(long stravaAccessTokenExpiration) {
        setStravaAccessTokenExpiration(stravaAccessTokenExpiration);
        return this;
    }

    public UserStrava stravaRefreshToken(String stravaRefreshToken) {
        setStravaRefreshToken(stravaRefreshToken);
        return this;
    }

    public UserStrava stravaUploadActivity(boolean stravaUploadActivity) {
        setStravaUploadActivity(stravaUploadActivity);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserStrava)) {
            return false;
        }
        UserStrava userStrava = (UserStrava) o;
        return Objects.equals(id, userStrava.id) && Objects.equals(userEmail, userStrava.userEmail) && stravaId == userStrava.stravaId && Objects.equals(stravaAccessToken, userStrava.stravaAccessToken) && stravaAccessTokenExpiration == userStrava.stravaAccessTokenExpiration && Objects.equals(stravaRefreshToken, userStrava.stravaRefreshToken) && stravaUploadActivity == userStrava.stravaUploadActivity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userEmail, stravaId, stravaAccessToken, stravaAccessTokenExpiration, stravaRefreshToken, stravaUploadActivity);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", userEmail='" + getUserEmail() + "'" +
            ", stravaId='" + getStravaId() + "'" +
            ", stravaAccessToken='" + getStravaAccessToken() + "'" +
            ", stravaAccessTokenExpiration='" + getStravaAccessTokenExpiration() + "'" +
            ", stravaRefreshToken='" + getStravaRefreshToken() + "'" +
            ", stravaUploadActivity='" + isStravaUploadActivity() + "'" +
            "}";
    }

}
