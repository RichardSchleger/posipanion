package sk.richardschleger.posipanion.entities;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import sk.richardschleger.posipanion.keys.UserUserId;

@Entity
@Table(name = "friends")
public class Friends {
    
    @EmbeddedId
    private UserUserId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user1_id")
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user2_id")
    private User user2;

    private boolean confirmed;

    public Friends() {
    }

    public Friends(UserUserId id, User user1, User user2, boolean confirmed) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.confirmed = confirmed;
    }

    public UserUserId getId() {
        return this.id;
    }

    public void setId(UserUserId id) {
        this.id = id;
    }

    public User getUser1() {
        return this.user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return this.user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public boolean isConfirmed() {
        return this.confirmed;
    }

    public boolean getConfirmed() {
        return this.confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Friends id(UserUserId id) {
        setId(id);
        return this;
    }

    public Friends user1(User user1) {
        setUser1(user1);
        return this;
    }

    public Friends user2(User user2) {
        setUser2(user2);
        return this;
    }

    public Friends confirmed(boolean confirmed) {
        setConfirmed(confirmed);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Friends)) {
            return false;
        }
        Friends userFriend = (Friends) o;
        return Objects.equals(id, userFriend.id) && Objects.equals(user1, userFriend.user1) && Objects.equals(user2, userFriend.user2) && confirmed == userFriend.confirmed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user1, user2, confirmed);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", user1='" + getUser1() + "'" +
            ", user2='" + getUser2() + "'" +
            ", confirmed='" + isConfirmed() + "'" +
            "}";
    }

}
