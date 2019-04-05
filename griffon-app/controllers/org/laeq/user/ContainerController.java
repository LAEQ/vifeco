package org.laeq.user;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.DAOException;
import org.laeq.db.UserDAO;
import org.laeq.model.User;
import org.laeq.service.MariaService;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends AbstractGriffonController{
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private ContainerView view;

    @Inject private MariaService dbService;
    @Inject protected DialogService dialogService;
    @Inject private PreferencesService preferencesService;

    private UserDAO userDAO;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        userDAO = dbService.getUserDAO();
        model.getUserList().addAll(userDAO.findAll());
        model.setPrefs(preferencesService.getPreferences());
        getApplication().getEventRouter().addEventListener(listeners());
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("change.language", objects -> {
            Locale locale = (Locale) objects[0];
            model.getPrefs().locale = locale;
            System.out.println("change.language");
            view.changeLocale();
        });

        return list;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void save(){
        if(model.valid()){
            User user = model.generateUser();
            if(model.getId() == 0) {
                insertUser(user);
            }else {
                updateUser(user);
            }
        } else {
            alert("org.laeq.title.error", String.format("Some fields are invalid: \n%s", model.getErrors()));
        }
    }

    private void insertUser(User user) {
        try {
            userDAO.insert(user);
            model.addUser(user);
            model.clearForm();
        } catch (DAOException e) {
            String message = String.format("Cannot save user: \n%s", user);
            alert("org.laeq.title.error", message);

            getLog().error(message);
        }
    }

    private void updateUser(User user) {
        try {
            userDAO.update(user);
            model.update(user);
            model.clearForm();
        } catch (DAOException | SQLException e) {
            String message = String.format("Cannot save user: \n%s", model.getUser());
            alert("org.laeq.title.error", message);

            getLog().error(message);
        }
    }

    public void delete(User user) {
        Boolean confirmation = confirm("org.laeq.user.delete.confirmation");

        if(confirmation){
            try {
                userDAO.delete(user);
                model.delete(user);
            } catch (DAOException e) {
                String message = getMessage("org.laeq.delete.default_user.error");
                alert("org.laeq.title.error", message);

                getLog().error(String.format("UserDAO: failed to deleteVideoIcon %s.", user));
            }
        }
    }


    protected void alert(String key, String alertMsg){
        runInsideUISync(() -> dialogService.simpleAlert(getMessage(key), alertMsg));
    }

    protected Boolean confirm(String key){
        return dialogService.confirm(getMessage(key));
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void clear(){
        model.clearForm();
    }

    protected String getMessage(String key){
        return getApplication().getMessageSource().getMessage(key, Locale.CANADA);
    }
}