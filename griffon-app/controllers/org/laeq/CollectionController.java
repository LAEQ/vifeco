package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;

import org.laeq.model.Collection;
import org.laeq.user.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class CollectionController extends CRUDController<Collection> {
    @Inject private DatabaseService dbService;
    @Inject private PreferencesService prefService;

    private TranslationService translationService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try {
            model.collections.addAll(dbService.collectionDAO.findAll());
            model.addCategories(dbService.categoryDAO.findAll());
            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("db.success.fetch"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.fetch"));
        }


        model.setPreferences(prefService.getPreferences());
        setTranslationService();

        view.initForm();

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void save(){
        try{
            Collection collection = model.getCollection();
            dbService.collectionDAO.create(collection);
            model.clear();
            model.collections.clear();
            model.collections.addAll(dbService.collectionDAO.findAll());

            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.success.save"));
        }catch (Exception e){
            System.out.println("Afficher message erreur collection");
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.save"));
        }
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void clear(){
        model.clear();
        view.clear();
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void delete(Collection collection) {
        try {
            dbService.collectionDAO.delete(collection);
            model.collections.clear();
            model.collections.addAll(dbService.collectionDAO.findAll());
        } catch (Exception e) {
            System.out.println("Afficher error collection delete");
        }
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("change.language", objects -> {
            Locale locale = (Locale) objects[0];
            model.getPreferences().locale = locale;
            setTranslationService();
            view.changeLocale();
        });

        return list;
    }

    private void setTranslationService(){
        try {
            translationService = new TranslationService(getClass().getClassLoader().getResourceAsStream("messages/messages.json"), model.getPreferences().locale);
            model.setTranslationService(translationService);
        } catch (IOException e) {
            getLog().error("Cannot load file messages.json");
        }
    }
}