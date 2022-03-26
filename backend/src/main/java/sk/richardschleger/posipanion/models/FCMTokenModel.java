package sk.richardschleger.posipanion.models;

import java.util.Objects;

public class FCMTokenModel {
    
    private String token;

    public FCMTokenModel() {
    }

    public FCMTokenModel(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public FCMTokenModel token(String token) {
        setToken(token);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FCMTokenModel)) {
            return false;
        }
        FCMTokenModel fCMTokenModel = (FCMTokenModel) o;
        return Objects.equals(token, fCMTokenModel.token);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token);
    }

    @Override
    public String toString() {
        return "{" +
            " token='" + getToken() + "'" +
            "}";
    }

}
