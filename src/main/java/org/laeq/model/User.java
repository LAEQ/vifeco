package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

import javax.annotation.Nonnull;
import java.util.Objects;

@JsonIgnoreProperties({"createdAt", "updatedAt"})
@JsonPropertyOrder({"id", "firstName", "lastName", "email", "isDefault"})
public class User extends BaseEntity {
    private int id;
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty email;
    private SimpleBooleanProperty isDefault;

    public User() {
        this.firstName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.isDefault = new SimpleBooleanProperty(false);
    }
    public User(Integer id, String fName, String lastName, String email) {
        this(fName, lastName, email);

        this.id = id;
    }
    public User(String fName, String lName, String email) {
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
        this.id = id;
    }
    public int getId() {
        return this.id;
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
        return id == user.getId() &&
                firstName.getValue().equals(user.firstName.getValue()) &&
                lastName.getValue().equals(user.lastName.getValue()) &&
                email.getValue().equals(user.email.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName.getValue(), lastName.getValue(), email.getValue());
    }

    @JsonIgnore
    public ObservableValue<String> getName() {
        return Bindings.createStringBinding(() -> {
            return String.format("%s %s", getFirstName(), getLastName());
        });
    }
}
