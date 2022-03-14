package sk.richardschleger.posipanion.entities;

import java.util.List;
import java.util.Objects;

import javax.persistence.*;


@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private StravaUser stravaUser;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ActiveUser activeUser;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FcmToken> fcmTokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Track> tracks;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserDetails userDetails;

    public User() {
    }

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StravaUser getStravaUser() {
        return stravaUser;
    }

    public void setStravaUser(StravaUser stravaUser) {
        this.stravaUser = stravaUser;
    }

    public ActiveUser getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(ActiveUser activeUser) {
        this.activeUser = activeUser;
    }

    public List<FcmToken> getFcmTokens() {
        return fcmTokens;
    }

    public void setFcmTokens(List<FcmToken> fcmTokens) {
        this.fcmTokens = fcmTokens;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public User id(int id) {
        setId(id);
        return this;
    }

    public User email(String email) {
        setEmail(email);
        return this;
    }

    public User password(String password) {
        setPassword(password);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && email.equals(user.email) && password.equals(user.password) && Objects.equals(stravaUser, user.stravaUser) && Objects.equals(activeUser, user.activeUser) && Objects.equals(fcmTokens, user.fcmTokens) && Objects.equals(tracks, user.tracks) && userDetails.equals(user.userDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, stravaUser, activeUser, fcmTokens, tracks, userDetails);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }

}
