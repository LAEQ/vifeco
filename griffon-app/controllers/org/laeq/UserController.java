package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.User;
import org.laeq.ui.DialogService;
import org.laeq.user.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class UserController extends AbstractGriffonController{
    @MVCMember @Nonnull private UserModel model;
    @MVCMember @Nonnull private UserView view;

    @Inject private DatabaseService dbService;
    @Inject protected DialogService dialogService;
    @Inject private PreferencesService preferencesService;

    private TranslationService translationService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try{
            model.userList.addAll(dbService.userDAO.findAll());
        } catch (Exception e){
            System.out.println("Cannot fetch users");
        }

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
        try {
            User user = model.getUser();
            dbService.userDAO.create(user);
            model.userList.clear();
            model.userList.addAll(dbService.userDAO.findAll());
            model.clear();
        }catch (Exception e){

        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void delete(User user) {
        try {
            dbService.userDAO.delete(user);
            model.userList.remove(user);
        } catch (RuntimeException e){
            System.out.println("AFFICHER UN MESSAGE user");
        } catch (Exception e){
            System.out.println("AFFICHER UN MESSAGE user");
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void clear(){
        model.clear();
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