package sk.richardschleger.posipanion.keys;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserUserId implements Serializable {
    
    @Column(name = "user1_id")
    private int user1Id;

    @Column(name = "user2_id")
    private int user2Id;

    public UserUserId() {
    }

    public UserUserId(int user1Id, int user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    public int getUser1Id() {
        return this.user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return this.user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public UserUserId user1Id(int user1Id) {
        setUser1Id(user1Id);
        return this;
    }

    public UserUserId user2Id(int user2Id) {
        setUser2Id(user2Id);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserUserId)) {
            return false;
        }
        UserUserId userUserId = (UserUserId) o;
        return user1Id == userUserId.user1Id && user2Id == userUserId.user2Id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1Id, user2Id);
    }

    @Override
    public String toString() {
        return "{" +
            " user1Id='" + getUser1Id() + "'" +
            ", user2Id='" + getUser2Id() + "'" +
            "}";
    }

}
