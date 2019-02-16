package org.laeq.category;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.SetChangeListener;
import javafx.scene.control.CheckBox;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import org.laeq.model.Category;
import org.laeq.model.CategoryCollection;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ArtifactProviderFor(GriffonController.class)
public class CategoryCollectionController extends AbstractGriffonController {
    @MVCMember @Nonnull private CategoryCollectionModel model;
    @MVCMember @Nonnull private CategoryCollectionView view;

    @Inject
    private DialogService dialogService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());

        getApplication().getEventRouter().publishEvent("database.category.list");
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void save() {
        Boolean result = true;
        StringBuilder builder = new StringBuilder("The following fields are invalid: \n");
        if(model.getName().length() == 0){
            builder.append(" - name\n");
            result = false;
        }

        if(! result){
            dialogService.dialog(builder.toString());
        } else {
            CategoryCollection entity = model.generateEntity();
            publishEvent("database.category_collection.create", entity);
        }
    }

    private void publishEvent(String eventName, CategoryCollection entity) {
        getApplication().getEventRouter().publishEventAsync(eventName, Arrays.asList(entity));
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void closeTab() {
        destroyMVCGroup(getMvcGroup().getMvcId());
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("category_collection.create.failed", objects -> {
            dialogService.dialog("Failed to save new category " + ((Exception) objects[0]).getMessage());
        });

        list.put("category_collection.created", objects -> {
            model.clear();
        });

        list.put("category.list", objects -> runInsideUISync(() -> {
            Set<Category> categorySet = (Set<Category>) objects[0];

            view.initPositions(categorySet.size());
            model.setCategories(categorySet);
        }));

        return list;
    }
}