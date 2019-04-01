package org.laeq.category;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.apache.batik.transcoder.TranscoderException;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.CategoryDAO;
import org.laeq.db.DAOException;
import org.laeq.icon.IconService;
import org.laeq.model.Category;
import org.laeq.service.MariaService;
import org.laeq.ui.DialogService;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends AbstractGriffonController {
    @MVCMember @Nonnull private ContainerModel model;
    @Inject private MariaService dbService;
    @Inject private DialogService dialogService;
    @Inject private IconService iconService;
    private CategoryDAO categoryDAO;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        categoryDAO = dbService.getCategoryDAO();
        model.getCategoryList().addAll(categoryDAO.findAll());
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
            alert(String.format("Some fields are invalid: \n%s", model.getErrors()));
        }
    }

    private void updateCategory() {
        Category category = model.generateEntity();

        try{
            categoryDAO.update(category);
            model.updateCategory(category);
            model.clear();
            iconService.createPNG(category);
        } catch (SQLException | DAOException | IOException | TranscoderException e) {
            getLog().error(e.getMessage());
            alert("Failed to update category: " + category.getName());
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
            alert("Failed to create category: " + category.getName());
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void clear(){
        model.clear();
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }

    private void alert(String alertMsg){
        dialogService.simpleAlert("org.laeq.title.error",alertMsg);
    }

    public void delete(Category category) {
        try{
            categoryDAO.delete(category);
            model.delete(category);
        } catch (DAOException e) {
            getLog().error(e.getMessage());
            dialogService.simpleAlert("org.laeq.title.error", e.getMessage());
        }
    }
}