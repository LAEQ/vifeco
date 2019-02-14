package org.laeq.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import javax.annotation.Nonnull;
import java.util.Objects;

public class User extends Entity{
    private SimpleIntegerProperty id;
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty email;
    private SimpleBooleanProperty isActive;

    public User() {
        this.id = new SimpleIntegerProperty(0);
        this.firstName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.isActive = new SimpleBooleanProperty(false);
    }

    public User(Integer id, String fName, String lastName, String email) {
        this(fName, lastName, email);
        this.id.set(id);
    }

    public User(String fName, String lName, String email) {
        this.id = new SimpleIntegerProperty(0);
        this.firstName = new SimpleStringProperty(fName);
        this.lastName = new SimpleStringProperty(lName);
        this.email = new SimpleStringProperty(email);
        this.isActive = new SimpleBooleanProperty(false);
    }

    public String getFirstName() {
        return firstName.get();
    }
    public void setFirstName(@Nonnull String fName) {
        firstName.set(fName);
    }

    public String getLastName() {
        return lastName.get();
    }
    public void setLastName(@Nonnull String fName) {
        lastName.set(fName);
    }

    public String getEmail() {
        return email.get();
    }
    public void setEmail(@Nonnull String fName) {
        email.set(fName);
    }

    public void setId(int id) {
        this.id.set(id);
    }

    @Override
    public int getId() {
        return this.id.getValue();
    }

    public boolean getIsActive() {
        return isActive.get();
    }
    public SimpleBooleanProperty isActiveProperty() {
        return isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    @Override
    public String toString() {
        return getName().getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.getValue().equals(user.id.getValue()) &&
                firstName.getValue().equals(user.firstName.getValue()) &&
                lastName.getValue().equals(user.lastName.getValue()) &&
                email.getValue().equals(user.email.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.getValue(), firstName.getValue(), lastName.getValue(), email.getValue());
    }

    public ObservableValue<String> getName() {
        return Bindings.createStringBinding(() -> {
            return String.format("%s %s", getFirstName(), getLastName());
        });
    }
}
