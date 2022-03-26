package sk.richardschleger.posipanion.entities;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "fcm_tokens")
public class FcmToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public FcmToken() {
    }

    public FcmToken(int id, String token, User user) {
        this.id = id;
        this.token = token;
        this.user = user;
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

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FcmToken id(int id) {
        setId(id);
        return this;
    }

    public FcmToken token(String token) {
        setToken(token);
        return this;
    }

    public FcmToken user(User user) {
        setUser(user);
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
        return id == fcmToken.id && Objects.equals(token, fcmToken.token) && Objects.equals(user, fcmToken.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, user);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", token='" + getToken() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }

}
