package sk.richardschleger.posipanion.entities;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "active_users")
public class ActiveUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "track_id", referencedColumnName = "track_id")
    private Track selected_track;

    @Column(name = "last_latitude")
    private double lastKnownLatitude;

    @Column(name = "last_longitude")
    private double lastKnownLongitude;

    public ActiveUser() {
    }

    public ActiveUser(int id, User user, Track selected_track, double lastKnownLatitude, double lastKnownLongitude) {
        this.id = id;
        this.user = user;
        this.selected_track = selected_track;
        this.lastKnownLatitude = lastKnownLatitude;
        this.lastKnownLongitude = lastKnownLongitude;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Track getSelected_track() {
        return this.selected_track;
    }

    public void setSelected_track(Track selected_track) {
        this.selected_track = selected_track;
    }

    public double getLastKnownLatitude() {
        return this.lastKnownLatitude;
    }

    public void setLastKnownLatitude(double lastKnownLatitude) {
        this.lastKnownLatitude = lastKnownLatitude;
    }

    public double getLastKnownLongitude() {
        return this.lastKnownLongitude;
    }

    public void setLastKnownLongitude(double lastKnownLongitude) {
        this.lastKnownLongitude = lastKnownLongitude;
    }

    public ActiveUser id(int id) {
        setId(id);
        return this;
    }

    public ActiveUser user(User user) {
        setUser(user);
        return this;
    }

    public ActiveUser selected_track(Track selected_track) {
        setSelected_track(selected_track);
        return this;
    }

    public ActiveUser lastKnownLatitude(double lastKnownLatitude) {
        setLastKnownLatitude(lastKnownLatitude);
        return this;
    }

    public ActiveUser lastKnownLongitude(double lastKnownLongitude) {
        setLastKnownLongitude(lastKnownLongitude);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ActiveUser)) {
            return false;
        }
        ActiveUser activeUser = (ActiveUser) o;
        return id == activeUser.id && Objects.equals(user, activeUser.user) && Objects.equals(selected_track, activeUser.selected_track) && lastKnownLatitude == activeUser.lastKnownLatitude && lastKnownLongitude == activeUser.lastKnownLongitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, selected_track, lastKnownLatitude, lastKnownLongitude);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", user='" + getUser() + "'" +
            ", selected_track='" + getSelected_track() + "'" +
            ", lastKnownLatitude='" + getLastKnownLatitude() + "'" +
            ", lastKnownLongitude='" + getLastKnownLongitude() + "'" +
            "}";
    }

}
