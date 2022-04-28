package sk.richardschleger.posipanion.entities;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "login_codes")
public class LoginCode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    private Timestamp expiresAt;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public LoginCode() {
    }

    public LoginCode(int id, String code, Timestamp expiresAt, User user) {
        this.id = id;
        this.code = code;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LoginCode id(int id) {
        setId(id);
        return this;
    }

    public LoginCode code(String code) {
        setCode(code);
        return this;
    }

    public LoginCode expiresAt(Timestamp expiresAt) {
        setExpiresAt(expiresAt);
        return this;
    }

    public LoginCode user(User user) {
        setUser(user);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof LoginCode)) {
            return false;
        }
        LoginCode loginCode = (LoginCode) o;
        return id == loginCode.id && Objects.equals(code, loginCode.code) && Objects.equals(expiresAt, loginCode.expiresAt) && Objects.equals(user, loginCode.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, expiresAt, user);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }

}
