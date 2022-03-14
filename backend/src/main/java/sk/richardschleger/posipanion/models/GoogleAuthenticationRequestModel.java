package sk.richardschleger.posipanion.models;

import java.util.Objects;

public class GoogleAuthenticationRequestModel {
    
    private String idToken;

    public GoogleAuthenticationRequestModel() {
    }

    public GoogleAuthenticationRequestModel(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return this.idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public GoogleAuthenticationRequestModel idToken(String idToken) {
        setIdToken(idToken);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof GoogleAuthenticationRequestModel)) {
            return false;
        }
        GoogleAuthenticationRequestModel googleAuthenticationRequestModel = (GoogleAuthenticationRequestModel) o;
        return Objects.equals(idToken, googleAuthenticationRequestModel.idToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idToken);
    }

    @Override
    public String toString() {
        return "{" +
            " idToken='" + getIdToken() + "'" +
            "}";
    }

}
