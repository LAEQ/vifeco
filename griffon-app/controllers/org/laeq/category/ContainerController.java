package org.laeq.category;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.apache.batik.transcoder.TranscoderException;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.TranslationService;
import org.laeq.db.CategoryDAO;
import org.laeq.db.DAOException;
import org.laeq.icon.IconService;
import org.laeq.model.Category;
import org.laeq.service.MariaService;
import org.laeq.ui.DialogService;
import org.laeq.user.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends AbstractGriffonController {
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private ContainerView view;

    @Inject private MariaService dbService;
    @Inject private DialogService dialogService;
    @Inject private IconService iconService;
    @Inject private PreferencesService preferencesService;
    private CategoryDAO categoryDAO;
    private TranslationService translationService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        categoryDAO = dbService.getCategoryDAO();
        model.getCategoryList().addAll(categoryDAO.findAll());

        model.setPrefs(preferencesService.getPreferences());
        setTranslationService();

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void save(){
        if(model.valid()){
            if(model.getId() != 0){
                updateCategory();
            } else {
                createCategory();
            }
        } else {
            alert(translationService.getMessage("org.laeq.model.invalid_fields") + "\n" + model.getErrors());
        }
    }

    private void updateCategory() {
        Category category = model.generateEntity();

        try{
            categoryDAO.update(category);
            model.updateCategory(category);
            model.clear();
            iconService.deletePNG(category);
            iconService.createPNG(category);
        } catch (SQLException | DAOException | IOException | TranscoderException e) {
            getLog().error(e.getMessage());
            alert(translationService.getMessage("")  + category.getName());
        }
    }

    private void createCategory() {
        Category category = model.generateEntity();
        try {
            categoryDAO.insert(category);
            model.addCategory(category);
            model.clear();
            iconService.createPNG(category);
        } catch (DAOException | IOException | TranscoderException e) {
            getLog().error(e.getMessage());
            alert(translationService.getMessage("org.laeq.category.save.error") + ": " + category.getName());
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void clear(){
        model.clear();
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

    private void alert(String alertMsg){
        dialogService.simpleAlert(translationService.getMessage("org.laeq.title.error") ,alertMsg);
    }

    public void delete(Category category) {
        try{
            categoryDAO.delete(category);
            model.delete(category);
            iconService.deletePNG(category);
        } catch (DAOException e) {
            getLog().error(e.getMessage());
            dialogService.simpleAlert(translationService.getMessage("org.laeq.title.error"), translationService.getMessage("org.laeq.category.delete.error") + ": " + category.getName());
        }
    }

    private void setTranslationService(){
        try {
            translationService = new TranslationService(getClass().getClassLoader().getResourceAsStream("messages/messages.json"), preferencesService.getPreferences().locale);
        } catch (IOException e) {
            getLog().error("Cannot load file messages.json");
        }
    }
}