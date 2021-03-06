package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.Type;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
@JsonIgnoreProperties({"default", "email"})
@JsonPropertyOrder({"id", "firstName", "lastName"})
final public class User
{
    @Id @GeneratedValue(generator = "increment")
    private Integer id;

    @Column(nullable = false)
    @Size(min = 1)
    private String firstName;

    @Column(nullable = false)
    @Size(min = 1)
    private String lastName;

    @Column(nullable = false)
    @Size(min = 1)
    private String email;

    @Type(type = "boolean")
    private Boolean isDefault = Boolean.FALSE;

    public User() {
    }

    public User(@Nonnull String firstName, @Nonnull String lastName, @Nonnull String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(Integer id, @Size(min = 1) String firstName, @Size(min = 1) String lastName, @Size(min = 1) String email, Boolean isDefault) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isDefault = isDefault;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Nonnull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Nonnull String firstName) {
        this.firstName = firstName;
    }

    @Nonnull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@Nonnull String lastName) {
        this.lastName = lastName;
    }

    @Nonnull
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nonnull String email) {
        this.email = email;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.firstName, this.lastName);
    }
}
