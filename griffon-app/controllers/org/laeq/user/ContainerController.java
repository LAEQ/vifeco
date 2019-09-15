package org.laeq.user;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.TranslationService;
import org.laeq.db.DAOException;
import org.laeq.db.UserDAO;
import org.laeq.model.User;
import org.laeq.service.MariaService;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
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
    private TranslationService translationService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        userDAO = dbService.getUserDAO();
        model.getUserList().addAll(userDAO.findAll());
        model.setPrefs(preferencesService.getPreferences());
        getApplication().getEventRouter().addEventListener(listeners());
        setTranslationService();
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("change.language", objects -> {
            Locale locale = (Locale) objects[0];
            model.getPrefs().locale = locale;
            setTranslationService();
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
            runInsideUISync(() -> dialogService.simpleAlert(translationService.getMessage("org.laeq.title.error"),
                    translationService.getMessage("org.laeq.model.invalid_fields") +  model.getErrors()));
        }
    }

    private void insertUser(User user) {
        try {
            userDAO.insert(user);
            model.addUser(user);
            model.clearForm();
        } catch (DAOException e) {
            runInsideUISync(() -> dialogService.simpleAlert(translationService.getMessage("org.laeq.title.error"), translationService.getMessage("org.laeq.user.save.error")));
            getLog().error(e.getMessage());
        }
    }

    private void updateUser(User user) {
        try {
            userDAO.update(user);
            model.update(user);
            model.clearForm();
        } catch (DAOException | SQLException e) {
            runInsideUISync(() -> dialogService.simpleAlert(translationService.getMessage("org.laeq.title.error"), translationService.getMessage("org.laeq.user.save.error")));

            getLog().error(e.getMessage());
        }
    }

    public void delete(User user) {
        Boolean confirmation = confirm("org.laeq.user.delete.confirmation");

        if(confirmation){
            try {
                userDAO.delete(user);
                model.delete(user);
            } catch (DAOException e) {
                runInsideUISync(() -> dialogService.simpleAlert(translationService.getMessage("org.laeq.title.error"), translationService.getMessage("org.laeq.user.delete.error")));
                getLog().error(e.getMessage());
            }
        }
    }


    protected Boolean confirm(String key){
        return dialogService.confirm(translationService.getMessage(key));
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void clear(){
        model.clearForm();
    }

    private void setTranslationService(){
        try {
            translationService = new TranslationService(getClass().getClassLoader().getResourceAsStream("messages/messages.json"), model.getPrefs().locale);
            model.setTranslationService(translationService);
        } catch (IOException e) {
            getLog().error("Cannot load file messages.json");
        }
    }
}