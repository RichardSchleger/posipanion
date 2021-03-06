package sk.richardschleger.posipanion.models;

import java.sql.Timestamp;
import java.util.Objects;

public class StravaModel {
    
    private long id;

    private String accessToken;

    private Timestamp accessTokenExpiration;

    private String refreshToken;

    private boolean upload;

    public StravaModel() {
    }

    public StravaModel(long id, String accessToken, Timestamp accessTokenExpiration, String refreshToken, boolean upload) {
        this.id = id;
        this.accessToken = accessToken;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshToken = refreshToken;
        this.upload = upload;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Timestamp getAccessTokenExpiration() {
        return this.accessTokenExpiration;
    }

    public void setAccessTokenExpiration(Timestamp accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isUpload() {
        return this.upload;
    }

    public boolean getUpload() {
        return this.upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public StravaModel id(long id) {
        setId(id);
        return this;
    }

    public StravaModel accessToken(String accessToken) {
        setAccessToken(accessToken);
        return this;
    }

    public StravaModel accessTokenExpiration(Timestamp accessTokenExpiration) {
        setAccessTokenExpiration(accessTokenExpiration);
        return this;
    }

    public StravaModel refreshToken(String refreshToken) {
        setRefreshToken(refreshToken);
        return this;
    }

    public StravaModel upload(boolean upload) {
        setUpload(upload);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StravaModel)) {
            return false;
        }
        StravaModel stravaModel = (StravaModel) o;
        return id == stravaModel.id && Objects.equals(accessToken, stravaModel.accessToken) && Objects.equals(accessTokenExpiration, stravaModel.accessTokenExpiration) && Objects.equals(refreshToken, stravaModel.refreshToken) && upload == stravaModel.upload;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accessToken, accessTokenExpiration, refreshToken, upload);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", accessToken='" + getAccessToken() + "'" +
            ", accessTokenExpiration='" + getAccessTokenExpiration() + "'" +
            ", refreshToken='" + getRefreshToken() + "'" +
            ", upload='" + isUpload() + "'" +
            "}";
    }

}
