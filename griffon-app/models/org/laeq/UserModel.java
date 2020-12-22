package org.laeq;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.TranslationService;
import org.laeq.model.Preferences;
import org.laeq.model.User;

import java.util.Collection;

@ArtifactProviderFor(GriffonModel.class)
public class UserModel extends AbstractGriffonModel {
    private TranslationService translationService;
    public ObservableList<User> userList = FXCollections.observableArrayList();

    public SimpleStringProperty firstName = new SimpleStringProperty(this, "firstName", "");
    public SimpleStringProperty lastName = new SimpleStringProperty(this, "lastName", "");
    public SimpleStringProperty email = new SimpleStringProperty(this, "email", "");

    private User selectedUser;
    private Preferences prefs;

    public void setTranslationService(TranslationService translationService) {
        this.translationService = translationService;
    }

    public String getErrors() {
        return "errors";
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
        this.firstName.set(selectedUser.getFirstName());
        this.lastName.set(selectedUser.getLastName());
        this.email.set(selectedUser.getEmail());
    }

    public void delete(User user) {
        userList.remove(user);
    }

    public Preferences getPrefs() {
        return prefs;
    }

    public void setPrefs(Preferences prefs) {
        this.prefs = prefs;
    }

    public void clear() {
        this.selectedUser = null;
        this.firstName.set("");
        this.lastName.set("");
        this.email.set("");
    }

    public User getUser() {
        if(this.selectedUser == null){
            this.selectedUser = new User();
        }

        this.selectedUser.setFirstName(firstName.get());
        this.selectedUser.setLastName(lastName.get());
        this.selectedUser.setEmail(email.get());

        return this.selectedUser;
    }
}