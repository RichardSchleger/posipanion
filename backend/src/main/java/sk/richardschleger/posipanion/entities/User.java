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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private LoginCode loginCode;

    public User() {
    }

    public User(int id, String email, String password, StravaUser stravaUser, ActiveUser activeUser, List<FcmToken> fcmTokens, List<Track> tracks, UserDetails userDetails, LoginCode loginCode) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.stravaUser = stravaUser;
        this.activeUser = activeUser;
        this.fcmTokens = fcmTokens;
        this.tracks = tracks;
        this.userDetails = userDetails;
        this.loginCode = loginCode;
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
        return this.stravaUser;
    }

    public void setStravaUser(StravaUser stravaUser) {
        this.stravaUser = stravaUser;
    }

    public ActiveUser getActiveUser() {
        return this.activeUser;
    }

    public void setActiveUser(ActiveUser activeUser) {
        this.activeUser = activeUser;
    }

    public List<FcmToken> getFcmTokens() {
        return this.fcmTokens;
    }

    public void setFcmTokens(List<FcmToken> fcmTokens) {
        this.fcmTokens = fcmTokens;
    }

    public List<Track> getTracks() {
        return this.tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public UserDetails getUserDetails() {
        return this.userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public LoginCode getLoginCode() {
        return this.loginCode;
    }

    public void setLoginCode(LoginCode loginCode) {
        this.loginCode = loginCode;
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

    public User stravaUser(StravaUser stravaUser) {
        setStravaUser(stravaUser);
        return this;
    }

    public User activeUser(ActiveUser activeUser) {
        setActiveUser(activeUser);
        return this;
    }

    public User fcmTokens(List<FcmToken> fcmTokens) {
        setFcmTokens(fcmTokens);
        return this;
    }

    public User tracks(List<Track> tracks) {
        setTracks(tracks);
        return this;
    }

    public User userDetails(UserDetails userDetails) {
        setUserDetails(userDetails);
        return this;
    }

    public User loginCode(LoginCode loginCode) {
        setLoginCode(loginCode);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(stravaUser, user.stravaUser) && Objects.equals(activeUser, user.activeUser) && Objects.equals(fcmTokens, user.fcmTokens) && Objects.equals(tracks, user.tracks) && Objects.equals(userDetails, user.userDetails) && Objects.equals(loginCode, user.loginCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, stravaUser, activeUser, fcmTokens, tracks, userDetails, loginCode);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", stravaUser='" + getStravaUser() + "'" +
            ", activeUser='" + getActiveUser() + "'" +
            ", fcmTokens='" + getFcmTokens() + "'" +
            ", tracks='" + getTracks() + "'" +
            ", userDetails='" + getUserDetails() + "'" +
            ", loginCode='" + getLoginCode() + "'" +
            "}";
    }

}
