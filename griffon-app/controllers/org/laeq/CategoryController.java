package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.icon.IconService;
import org.laeq.model.Category;
import org.laeq.user.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class CategoryController extends AbstractGriffonController {
    @MVCMember @Nonnull private CategoryModel model;
    @MVCMember @Nonnull private CategoryView view;

    @Inject private DatabaseService dbService;
    @Inject private IconService iconService;
    @Inject private PreferencesService preferencesService;
    private TranslationService translationService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try{
            model.categoryList.addAll(dbService.categoryDAO.findAll());
            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("db.success.fetch"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.fetch"));
        }

        model.setPrefs(preferencesService.getPreferences());
        setTranslationService();

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void save(){
        try{
            dbService.categoryDAO.create(model.getCategory());
            model.categoryList.clear();
            model.clear();
            model.categoryList.addAll(dbService.categoryDAO.findAll());
            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("db.success.save"));

        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.save"));
        }
    }

    private void createCategory() {
//        iconService.createPNG(category);
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void clear(){
        model.clear();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void delete(Category category) {
        try{
            dbService.categoryDAO.delete(category);
            model.categoryList.remove(category);
            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("db.success.delete"));
        }  catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.delete"));
        }
    }

    private void setTranslationService(){
        try {
            translationService = new TranslationService(getClass().getClassLoader().getResourceAsStream("messages/messages.json"), preferencesService.getPreferences().locale);
            model.setTranslationService(translationService);
        } catch (IOException e) {
            getLog().error("Cannot load file messages.json");
        }
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }
}