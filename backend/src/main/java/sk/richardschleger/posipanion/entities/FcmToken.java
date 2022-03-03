package sk.richardschleger.posipanion.entities;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "fcm_tokens")
public class FcmToken {
    
    @Id
    private int id;

    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public FcmToken() {
    }

    public FcmToken(int id, String token) {
        this.id = id;
        this.token = token;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public FcmToken id(int id) {
        setId(id);
        return this;
    }

    public FcmToken token(String token) {
        setToken(token);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FcmToken)) {
            return false;
        }
        FcmToken fcmToken = (FcmToken) o;
        return id == fcmToken.id && Objects.equals(token, fcmToken.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", token='" + getToken() + "'" +
            "}";
    }

}
