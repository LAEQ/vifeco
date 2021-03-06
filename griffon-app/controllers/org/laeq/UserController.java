package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.User;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class UserController extends AbstractGriffonController{
    @MVCMember @Nonnull private UserModel model;
    @MVCMember @Nonnull private UserView view;

    @Inject private DatabaseService dbService;
    @Inject private PreferencesService preferencesService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try{
            model.userList.addAll(dbService.userDAO.findAll());
            getApplication().getEventRouter().publishEventOutsideUI("status.info", Arrays.asList("db.user.fetch.success"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.user.fetch.error"));
        }

        getApplication().getEventRouter().addEventListener(listeners());
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void save(){
        try {
            User user = model.getUser();
            dbService.userDAO.create(user);
            model.userList.clear();
            model.userList.addAll(dbService.userDAO.findAll());
            model.clear();
            getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("db.user.save.success", user.toString()));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.user.save.error"));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void delete(User user) {
        try {
            dbService.userDAO.delete(user);
            model.userList.remove(user);
            getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("db.user.delete.success", user.toString()));
        }  catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.user.delete.error"));
        }
    }
}