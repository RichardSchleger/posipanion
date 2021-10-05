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
@Table(name = "friends")
public class Friend {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user1_id", referencedColumnName = "id")
    private User user1;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user2_id", referencedColumnName = "id")
    private User user2;

    private boolean confirmed;

    public Friend() {
    }

    public Friend(int id, User user1, User user2, boolean confirmed) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.confirmed = confirmed;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
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

    public Friend id(int id) {
        setId(id);
        return this;
    }

    public Friend user1(User user1) {
        setUser1(user1);
        return this;
    }

    public Friend user2(User user2) {
        setUser2(user2);
        return this;
    }

    public Friend confirmed(boolean confirmed) {
        setConfirmed(confirmed);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Friend)) {
            return false;
        }
        Friend friend = (Friend) o;
        return id == friend.id && Objects.equals(user1, friend.user1) && Objects.equals(user2, friend.user2) && confirmed == friend.confirmed;
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
