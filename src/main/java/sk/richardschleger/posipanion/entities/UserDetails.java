package sk.richardschleger.posipanion.entities;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class UserDetails {
    
    @PrimaryKey
    private String email;

    private String firstName;

    private String surname;

    private Set<String> fcmTokens;

    private UUID currentTrackId;

    private UUID selectedTrackId;

    public UserDetails() {
    }

    public UserDetails(String email, String firstName, String surname, Set<String> fcmTokens, UUID currentTrackId, UUID selectedTrackId) {
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.fcmTokens = fcmTokens;
        this.currentTrackId = currentTrackId;
        this.selectedTrackId = selectedTrackId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public UUID getCurrentTrackId() {
        return this.currentTrackId;
    }

    public void setCurrentTrackId(UUID currentTrackId) {
        this.currentTrackId = currentTrackId;
    }

    public UUID getSelectedTrackId() {
        return this.selectedTrackId;
    }

    public void setSelectedTrackId(UUID selectedTrackId) {
        this.selectedTrackId = selectedTrackId;
    }

    public UserDetails email(String email) {
        setEmail(email);
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

    public UserDetails currentTrackId(UUID currentTrackId) {
        setCurrentTrackId(currentTrackId);
        return this;
    }

    public UserDetails selectedTrackId(UUID selectedTrackId) {
        setSelectedTrackId(selectedTrackId);
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
        return Objects.equals(email, userDetails.email) && Objects.equals(firstName, userDetails.firstName) && Objects.equals(surname, userDetails.surname) && Objects.equals(fcmTokens, userDetails.fcmTokens) && Objects.equals(currentTrackId, userDetails.currentTrackId) && Objects.equals(selectedTrackId, userDetails.selectedTrackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, surname, fcmTokens, currentTrackId, selectedTrackId);
    }

    @Override
    public String toString() {
        return "{" +
            " email='" + getEmail() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", fcmTokens='" + getFcmTokens() + "'" +
            ", currentTrackId='" + getCurrentTrackId() + "'" +
            ", selectedTrackId='" + getSelectedTrackId() + "'" +
            "}";
    }

}
