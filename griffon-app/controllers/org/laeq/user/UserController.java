package org.laeq.user;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import org.laeq.model.Category;
import org.laeq.model.User;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class UserController extends AbstractGriffonController {
    @MVCMember @Nonnull private UserModel model;
    @Inject private DialogService dialogService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void closeTab() {
        destroyMVCGroup(getMvcGroup().getMvcId());
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void save(){
        boolean result = true;
        StringBuilder builder = new StringBuilder();

        builder.append("The following fields are incorrect: \n");

        if(model.getFirstName().length() == 0){
            builder.append(" - first name \n");
            result = false;
        }

        if(model.getLastName().length() == 0){
            result = false;
            builder.append(" - last name \n");

        }

        if(model.getEmail().length() == 0){
            result = false;
            builder.append(" - email \n");

        }

        if(result == false){
            dialogService.dialog(builder.toString());
        } else {
            User user = new User();
            user.setFirstName(model.getFirstName());
            user.setLastName(model.getLastName());
            user.setEmail(model.getEmail());
            getApplication().getEventRouter().publishEventAsync("database.user.new", Arrays.asList(user));
        }
    }
}