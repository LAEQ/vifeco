package org.laeq.category;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import org.laeq.model.Category;
import org.laeq.model.User;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ArtifactProviderFor(GriffonController.class)
public class CategoryListController extends AbstractGriffonController {
    @MVCMember @Nonnull private CategoryListModel model;
    @MVCMember @Nonnull private CategoryListView view;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listenerList());
        getApplication().getEventRouter().publishEvent("database.category.list");
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void closeTab() {
        destroyMVCGroup(getMvcGroup().getMvcId());
    }

    private Map<String, RunnableWithArgs> listenerList() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("find.category.list", objects -> runInsideUISync(() -> {
            Set<Category> categorySet = (Set<Category>) objects[0];
            model.add(categorySet);
            view.init();
        }));

        list.put("category.created", objects -> {
            Category category = (Category) objects[0];
            model.getCategoryList().add(category);
        });

        list.put("category.delete", objects -> {
            Category category = (Category) objects[0];
            model.delete(category);
        });


        return list;
    }

    public void delete(Category category) {
    }
}