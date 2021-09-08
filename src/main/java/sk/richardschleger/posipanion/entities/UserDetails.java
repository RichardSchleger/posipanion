package sk.richardschleger.posipanion.entities;

import java.util.Objects;
import java.util.Set;

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
@Table(name = "user_details")
public class UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    private String surname;

    @Column(name = "fcm_tokens")
    private Set<String> fcmTokens;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public UserDetails() {
    }

    public UserDetails(int id, String firstName, String surname, Set<String> fcmTokens, User user) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.fcmTokens = fcmTokens;
        this.user = user;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<String> getFcmTokens() {
        return this.fcmTokens;
    }

    public void setFcmTokens(Set<String> fcmTokens) {
        this.fcmTokens = fcmTokens;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserDetails id(int id) {
        setId(id);
        return this;
    }

    public UserDetails firstName(String firstName) {
        setFirstName(firstName);
        return this;
    }

    public UserDetails surname(String surname) {
        setSurname(surname);
        return this;
    }

    public UserDetails fcmTokens(Set<String> fcmTokens) {
        setFcmTokens(fcmTokens);
        return this;
    }

    public UserDetails user(User user) {
        setUser(user);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserDetails)) {
            return false;
        }
        UserDetails userDetails = (UserDetails) o;
        return id == userDetails.id && Objects.equals(firstName, userDetails.firstName) && Objects.equals(surname, userDetails.surname) && Objects.equals(fcmTokens, userDetails.fcmTokens) && Objects.equals(user, userDetails.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, surname, fcmTokens, user);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", fcmTokens='" + getFcmTokens() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }

}
