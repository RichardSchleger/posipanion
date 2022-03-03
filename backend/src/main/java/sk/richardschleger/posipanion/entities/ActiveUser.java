package sk.richardschleger.posipanion.entities;

import java.util.Objects;

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

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = "track_id", referencedColumnName = "track_id")
    private Track selectedTrack;

    @Column(name = "last_latitude")
    private double lastKnownLatitude;

    @Column(name = "last_longitude")
    private double lastKnownLongitude;

    public ActiveUser() {
    }

    public ActiveUser(int id, User user, Track selectedTrack, double lastKnownLatitude, double lastKnownLongitude) {
        this.id = id;
        this.user = user;
        this.selectedTrack = selectedTrack;
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

    public Track getSelectedTrack() {
        return this.selectedTrack;
    }

    public void setSelectedTrack(Track selectedTrack) {
        this.selectedTrack = selectedTrack;
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

    public ActiveUser selectedTrack(Track selectedTrack) {
        setSelectedTrack(selectedTrack);
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
        return id == activeUser.id && Objects.equals(user, activeUser.user) && Objects.equals(selectedTrack, activeUser.selectedTrack) && lastKnownLatitude == activeUser.lastKnownLatitude && lastKnownLongitude == activeUser.lastKnownLongitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, selectedTrack, lastKnownLatitude, lastKnownLongitude);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", user='" + getUser() + "'" +
            ", selected_track='" + getSelectedTrack() + "'" +
            ", lastKnownLatitude='" + getLastKnownLatitude() + "'" +
            ", lastKnownLongitude='" + getLastKnownLongitude() + "'" +
            "}";
    }

}
