package org.laeq.category;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import org.laeq.db.CategoryDAO;
import org.laeq.db.DAOException;
import org.laeq.db.DatabaseService;
import org.laeq.model.Category;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends AbstractGriffonController {
    @MVCMember @Nonnull private ContainerModel model;
    @Inject private DatabaseService dbService;
    @Inject private DialogService dialogService;

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
           createCategory();
        } else {
            alert(String.format("Some fields are invalid: \n%s", model.getErrors()));
        }
    }

    private void createCategory() {
        Category category = model.generateEntity();
        try {
            categoryDAO.insert(category);
            model.addCategory(category);
        } catch (DAOException e) {
            alert("Failed to create category: " + category);
        }
    }


    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }

    private void alert(String alertMsg){
        dialogService.simpleAlert(alertMsg);
    }
}