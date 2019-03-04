package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import javax.annotation.Nonnull;
import java.util.Objects;

@JsonIgnoreProperties({"createdAt", "updatedAt"})
public class User extends BaseEntity {
    private SimpleIntegerProperty id;
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty email;
    private SimpleBooleanProperty isDefault;

    public User() {
        this.id = new SimpleIntegerProperty(0);
        this.firstName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.isDefault = new SimpleBooleanProperty(false);
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
        this.isDefault = new SimpleBooleanProperty(false);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String fName) {
        firstName.set(fName);
    }

    public String getLastName() {
        return lastName.get();
    }

    @JsonIgnore
    public SimpleIntegerProperty idProperty() {
        return id;
    }
    @JsonIgnore
    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }
    @JsonIgnore
    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }
    @JsonIgnore
    public SimpleStringProperty emailProperty() {
        return email;
    }

    public boolean getIsDefault() {
        return isDefault.get();
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
    public int getId() {
        return this.id.getValue();
    }
    public boolean getIsActive() {
        return isDefault.get();
    }
    public void setIsDefault(boolean isDefault) {
        this.isDefault.set(isDefault);
    }

    @JsonIgnore
    public SimpleBooleanProperty isDefaultProperty() {
        return isDefault;
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

    @JsonIgnore
    public ObservableValue<String> getName() {
        return Bindings.createStringBinding(() -> {
            return String.format("%s %s", getFirstName(), getLastName());
        });
    }
}
