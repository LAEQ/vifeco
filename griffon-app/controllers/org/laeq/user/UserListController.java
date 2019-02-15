package org.laeq.user;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import org.laeq.model.Point;
import org.laeq.model.User;
import org.laeq.model.VideoUser;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.*;

@ArtifactProviderFor(GriffonController.class)
public class UserListController extends AbstractGriffonController {
    private UserListModel model;
    private UserListView view;

    @MVCMember
    public void setView(@Nonnull UserListView view){
        this.view = view;
    }

    @MVCMember
    public void setModel(@Nonnull UserListModel model) {
        this.model = model;
    }

    @Inject
    private DialogService dialogService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listenerList());
        getApplication().getEventRouter().publishEvent("database.user.list");
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void closeTab() {
        destroyMVCGroup(getMvcGroup().getMvcId());
    }


    private Map<String, RunnableWithArgs> listenerList(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("user.list", objects -> runInsideUISync(() -> {
            Set<User> userSet = (Set<User>) objects[0];
            model.getUserList().addAll(userSet);
        }));

        list.put("user.delete", objects -> {
            User user = (User) objects[0];
            model.delete(user);
        });

        list.put("user.created", objects -> {
            User user = (User) objects[0];
            model.getUserList().add(user);
        });

        list.put("user.active", objects -> {
            User user = (User) objects[0];
            model.changeActiveUser(user);
        });

        return list;
    }

    public void delete(User user) {
        publishEvent("database.user.delete", user);
    }

    private void publishEvent(String eventName, Object obj){
        getApplication().getEventRouter().publishEventAsync(eventName, Arrays.asList(obj));
    }
}