package sk.richardschleger.posipanion.entities;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "strava_users")
public class StravaUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private long stravaId;

    private String stravaAccessToken;

    private long stravaAccessTokenExpiration;

    private String stravaRefreshToken;

    private boolean stravaUploadActivity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public StravaUser() {
    }

    public StravaUser(int id, long stravaId, String stravaAccessToken, long stravaAccessTokenExpiration, String stravaRefreshToken, boolean stravaUploadActivity, User user) {
        this.id = id;
        this.stravaId = stravaId;
        this.stravaAccessToken = stravaAccessToken;
        this.stravaAccessTokenExpiration = stravaAccessTokenExpiration;
        this.stravaRefreshToken = stravaRefreshToken;
        this.stravaUploadActivity = stravaUploadActivity;
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

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StravaUser id(int id) {
        setId(id);
        return this;
    }

    public StravaUser stravaId(long stravaId) {
        setStravaId(stravaId);
        return this;
    }

    public StravaUser stravaAccessToken(String stravaAccessToken) {
        setStravaAccessToken(stravaAccessToken);
        return this;
    }

    public StravaUser stravaAccessTokenExpiration(long stravaAccessTokenExpiration) {
        setStravaAccessTokenExpiration(stravaAccessTokenExpiration);
        return this;
    }

    public StravaUser stravaRefreshToken(String stravaRefreshToken) {
        setStravaRefreshToken(stravaRefreshToken);
        return this;
    }

    public StravaUser stravaUploadActivity(boolean stravaUploadActivity) {
        setStravaUploadActivity(stravaUploadActivity);
        return this;
    }

    public StravaUser user(User user) {
        setUser(user);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StravaUser)) {
            return false;
        }
        StravaUser stravaUser = (StravaUser) o;
        return id == stravaUser.id && stravaId == stravaUser.stravaId && Objects.equals(stravaAccessToken, stravaUser.stravaAccessToken) && stravaAccessTokenExpiration == stravaUser.stravaAccessTokenExpiration && Objects.equals(stravaRefreshToken, stravaUser.stravaRefreshToken) && stravaUploadActivity == stravaUser.stravaUploadActivity && Objects.equals(user, stravaUser.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stravaId, stravaAccessToken, stravaAccessTokenExpiration, stravaRefreshToken, stravaUploadActivity, user);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", stravaId='" + getStravaId() + "'" +
            ", stravaAccessToken='" + getStravaAccessToken() + "'" +
            ", stravaAccessTokenExpiration='" + getStravaAccessTokenExpiration() + "'" +
            ", stravaRefreshToken='" + getStravaRefreshToken() + "'" +
            ", stravaUploadActivity='" + isStravaUploadActivity() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }

}
