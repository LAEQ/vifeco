package org.laeq.user;

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
public class ContainerModel extends AbstractGriffonModel {
    private TranslationService translationService;
    //Table section
    private ObservableList<User> userList = FXCollections.observableArrayList();

    // Form Section
    private SimpleIntegerProperty id = new SimpleIntegerProperty(this, "id", 0);
    private SimpleStringProperty firstName = new SimpleStringProperty(this, "firstName", "");
    private SimpleStringProperty lastName = new SimpleStringProperty(this, "lastName", "");
    private SimpleStringProperty email = new SimpleStringProperty(this, "email", "");
    private String errors = "";

    private User selectedUser;

    private Preferences prefs;

    public ObservableList<User> getUserList() {
        return userList;
    }
    public void addUsers(Collection<User> users){
        userList.addAll(users);
    }

    public int getId() {
        return id.get();
    }
    public SimpleIntegerProperty idProperty() {
        return id;
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public String getFirstName() {
        return firstName.get();
    }
    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }
    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getEmail() {
        return email.get();
    }
    public SimpleStringProperty emailProperty() {
        return email;
    }
    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setTranslationService(TranslationService translationService) {
        this.translationService = translationService;
    }

    public Boolean valid(){
        Boolean result = true;
        errors = "\n";

        StringBuilder builder = new StringBuilder("\n");

        if(getFirstName().length() == 0){
            result = false;
            builder.append(translationService.getMessage("org.laeq.video.user.first_name.error"));
            builder.append("\n");
        }

        if(getLastName().length() == 0){
            result = false;
            builder.append(translationService.getMessage("org.laeq.video.user.last_name.error"));
            builder.append("\n");
        }

        if(getEmail().length() == 0){
            result = false;
            builder.append(translationService.getMessage("org.laeq.video.user.email.error"));
        }

        errors = builder.toString();

        return result;
    }

    public User generateUser() {
        return createUser();
    }

    private User createUser() {
        User user = new User();
        user.setId(getId());
        user.setFirstName(getFirstName());
        user.setLastName(getLastName());
//        user.setIsDefault(false);
        user.setEmail(getEmail());

        return user;
    }

    public void clearForm() {
        this.selectedUser = null;
        setFirstName("");
        setLastName("");
        setEmail("");
        setId(0);
        errors = "";
    }

    public String getErrors() {
        return errors;
    }

    public void setSelectedUser(User selectedUser) {
        if(selectedUser != null){
            this.selectedUser = selectedUser;
            setId(selectedUser.getId());
            setFirstName(selectedUser.getFirstName());
            setLastName(selectedUser.getLastName());
            setEmail(selectedUser.getEmail());
        } else {
            setId(0);
            setFirstName("");
            setLastName("");
            setEmail("");
        }
    }

    public User getUser() {
        return userList.stream().filter(u -> u.getId() == getId()).findFirst().get();
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public void update(User user) {
        selectedUser.setFirstName(user.getFirstName());
        selectedUser.setLastName(user.getLastName());
        selectedUser.setEmail(user.getEmail());
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
        setId(0);
        setFirstName("");
        setLastName("");
        setEmail("");
    }
}