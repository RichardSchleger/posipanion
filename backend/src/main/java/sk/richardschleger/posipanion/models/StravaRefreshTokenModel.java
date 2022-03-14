package sk.richardschleger.posipanion.models;

import java.util.Objects;

public class StravaRefreshTokenModel {
    
    private String access_token;

    private long expires_at;

    private int expires_in;

    private String refresh_token;

    private String token_type;

    public StravaRefreshTokenModel() {
    }

    public StravaRefreshTokenModel(String access_token, long expires_at, int expires_in, String refresh_token, String token_type) {
        this.access_token = access_token;
        this.expires_at = expires_at;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return this.access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_at() {
        return this.expires_at;
    }

    public void setExpires_at(long expires_at) {
        this.expires_at = expires_at;
    }

    public int getExpires_in() {
        return this.expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return this.refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getToken_type() {
        return this.token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public StravaRefreshTokenModel access_token(String access_token) {
        setAccess_token(access_token);
        return this;
    }

    public StravaRefreshTokenModel expires_at(long expires_at) {
        setExpires_at(expires_at);
        return this;
    }

    public StravaRefreshTokenModel expires_in(int expires_in) {
        setExpires_in(expires_in);
        return this;
    }

    public StravaRefreshTokenModel refresh_token(String refresh_token) {
        setRefresh_token(refresh_token);
        return this;
    }

    public StravaRefreshTokenModel token_type(String token_type) {
        setToken_type(token_type);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StravaRefreshTokenModel)) {
            return false;
        }
        StravaRefreshTokenModel stravaRefreshTokenModel = (StravaRefreshTokenModel) o;
        return Objects.equals(access_token, stravaRefreshTokenModel.access_token) && expires_at == stravaRefreshTokenModel.expires_at && expires_in == stravaRefreshTokenModel.expires_in && Objects.equals(refresh_token, stravaRefreshTokenModel.refresh_token) && Objects.equals(token_type, stravaRefreshTokenModel.token_type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access_token, expires_at, expires_in, refresh_token, token_type);
    }

    @Override
    public String toString() {
        return "{" +
            " access_token='" + getAccess_token() + "'" +
            ", expires_at='" + getExpires_at() + "'" +
            ", expires_in='" + getExpires_in() + "'" +
            ", refresh_token='" + getRefresh_token() + "'" +
            ", token_type='" + getToken_type() + "'" +
            "}";
    }

}
