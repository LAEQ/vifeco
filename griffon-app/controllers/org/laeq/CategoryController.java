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
        } catch (Exception e){
            System.out.println("Cannot fetch category");
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
        } catch (Exception e){
            System.out.println("AFFICHER UN MESSAGE category");
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

    public void delete(Category category) {
        try{
            dbService.categoryDAO.delete(category);
            model.categoryList.remove(category);
        }  catch (Exception e){
            System.out.println("AFFICHER UN MESSAGE category");
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
        list.put("change.language", objects -> {
            Locale locale = (Locale) objects[0];
            model.prefs.locale = locale;
            view.changeLocale(locale);
        });
        return list;
    }
}