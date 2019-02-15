package org.laeq.user;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.User;

import java.util.List;

@ArtifactProviderFor(GriffonModel.class)
public class UserListModel extends AbstractGriffonModel {
    private ObservableList<User> userList = FXCollections.observableArrayList();

    public void addUsers(List<User> list){
        userList.addAll(list);
    }
    public void addUser(User video){
        userList.add(video);
    }
    public ObservableList<User> getUserList() {
        return userList;
    }

    public void delete(User user) {
        getUserList().remove(user);
    }

    public void changeActiveUser(User user) {
        userList.forEach(user1 -> user1.setIsActive(false));

        userList.filtered(obj -> obj.equals(user)).forEach(obj -> obj.setIsActive(true));
    }
}