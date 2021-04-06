package org.laeq;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class UserModel extends AbstractGriffonModel {
    public ObservableList<User> userList = FXCollections.observableArrayList();
    public SimpleStringProperty firstName = new SimpleStringProperty(this, "firstName", "");
    public SimpleStringProperty lastName = new SimpleStringProperty(this, "lastName", "");

    private User selectedUser;

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
        this.firstName.set(selectedUser.getFirstName());
        this.lastName.set(selectedUser.getLastName());
    }

    public void delete(User user) {
        userList.remove(user);
    }

    public void clear() {
        this.selectedUser = null;
        this.firstName.set("");
        this.lastName.set("");
    }

    public Boolean isValid(){
        User user = new User();
        user.setFirstName(firstName.get());
        user.setLastName(lastName.get());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if(violations.size() > 0){
            return false;
        }

        return true;
    }

    public User getUser() {
        if(this.selectedUser == null){
            this.selectedUser = new User();
        }

        this.selectedUser.setFirstName(firstName.get());
        this.selectedUser.setLastName(lastName.get());

        return this.selectedUser;
    }
}