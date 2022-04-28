package sk.richardschleger.posipanion.models;

import java.sql.Timestamp;
import java.util.Objects;

public class LoginCodeModel {
    
    private String code;

    private Timestamp expiresAt;

    public LoginCodeModel() {
    }

    public LoginCodeModel(String code, Timestamp expiresAt) {
        this.code = code;
        this.expiresAt = expiresAt;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Timestamp getExpiresAt() {
        return this.expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LoginCodeModel code(String code) {
        setCode(code);
        return this;
    }

    public LoginCodeModel expiresAt(Timestamp expiresAt) {
        setExpiresAt(expiresAt);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof LoginCodeModel)) {
            return false;
        }
        LoginCodeModel loginCodeModel = (LoginCodeModel) o;
        return Objects.equals(code, loginCodeModel.code) && Objects.equals(expiresAt, loginCodeModel.expiresAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, expiresAt);
    }

    @Override
    public String toString() {
        return "{" +
            " code='" + getCode() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            "}";
    }

}
