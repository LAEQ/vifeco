package org.laeq.collection;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.laeq.CRUDController;
import org.laeq.TranslationService;
import org.laeq.db.CategoryDAO;
import org.laeq.db.CollectionDAO;
import org.laeq.db.DAOException;
import org.laeq.model.Collection;
import org.laeq.service.MariaService;
import org.laeq.user.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends CRUDController<Collection> {
    @Inject private MariaService dbService;
    @Inject private PreferencesService prefService;
    private CollectionDAO collectionDAO;
    private CategoryDAO categoryDAO;
    private TranslationService translationService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        collectionDAO = dbService.getCollectionDAO();
        categoryDAO = dbService.getCategoryDAO();
        model.getCollections().addAll(collectionDAO.findAll());
        model.addCategories(categoryDAO.findAll());

        model.setPreferences(prefService.getPreferences());
        setTranslationService();

        view.initForm();

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void save(){
        if(model.valid()){
            Collection collection = model.generateCollection();

            try {
                collectionDAO.save(collection);
                model.update(collection);
            } catch (DAOException e) {
                getLog().error(e.getMessage());
                alert(translationService.getMessage("org.laeq.title.error"),
                        translationService.getMessage("org.laeq.model.invalid_fields") + ": " + e.getMessage());
            }
        } else {
            getLog().error(model.getErrors());
            alert(translationService.getMessage("org.laeq.model.collection.form.alert.title"), model.getErrors());
        }
    }

    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void clear(){
        model.clearForm();
        view.clear();
    }

    public void delete(Collection collection) {
        runInsideUISync(() -> {
            if(confirm(translationService.getMessage("org.laeq.model.collection.delete.confirmation"))){
                try {
                    collectionDAO.delete(collection);
                    this.model.delete(collection);
                } catch (DAOException e) {
                    getLog().error(e.getMessage());
                    alert(translationService.getMessage("org.laeq.dao.error"),
                            translationService.getMessage("org.laeq.model.collection.default.error"));
                }
            }
        });
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