package sk.richardschleger.posipanion.models;

import java.util.Objects;

public class RegistrationModel {
    
    private String name;

    private String surname;

    private String email;

    private String password;

    public RegistrationModel() {
    }

    public RegistrationModel(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public RegistrationModel name(String name) {
        setName(name);
        return this;
    }

    public RegistrationModel surname(String surname) {
        setSurname(surname);
        return this;
    }

    public RegistrationModel email(String email) {
        setEmail(email);
        return this;
    }

    public RegistrationModel password(String password) {
        setPassword(password);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RegistrationModel)) {
            return false;
        }
        RegistrationModel registrationModel = (RegistrationModel) o;
        return Objects.equals(name, registrationModel.name) && Objects.equals(surname, registrationModel.surname) && Objects.equals(email, registrationModel.email) && Objects.equals(password, registrationModel.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, email, password);
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }

}
