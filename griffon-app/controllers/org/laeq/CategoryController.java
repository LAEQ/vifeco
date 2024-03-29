package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Category;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class CategoryController extends AbstractGriffonController implements CRUDInterface<Category> {
    @MVCMember @Nonnull private CategoryModel model;
    @MVCMember @Nonnull private CategoryView view;
    @Inject private DatabaseService dbService;


    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        runInsideUIAsync(() -> {
            try{
                model.categoryList.addAll(dbService.categoryDAO.findAll());
                getApplication().getEventRouter().publishEventAsync("status.info", Arrays.asList("db.category.fetch.success"));
            } catch (Exception e){
                getApplication().getEventRouter().publishEventAsync("status.error", Arrays.asList("db.category.fetch.error"));
            }
        });

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    @Override
    public void save(){
        if(model.isValid() == false){
            getApplication().getEventRouter().publishEventAsync("status.error", Arrays.asList("db.category.save.error"));
            return;
        }

        Category category = model.getCategory();
        if(dbService.categoryDAO.create(category)){
            if(model.categoryList.contains(category) == false){
                model.categoryList.addAll(category);
            }
            view.refresh();
            getApplication().getEventRouter().publishEventAsync("status.success.parametrized", Arrays.asList("db.category.save.success", category.toString()));
            model.clear();
        }else{
            getApplication().getEventRouter().publishEventAsync("status.error", Arrays.asList("db.category.save.error"));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    @Override
    public void clear(){
        model.clear();
        getApplication().getEventRouter().publishEvent("status.reset");
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    @Override
    public void delete(Category category) {
        if(dbService.categoryDAO.delete(category)){
            model.categoryList.remove(category);

            getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("db.category.delete.success", category.getName()));
            view.refresh();
            model.clear();
        }  else {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.category.delete.error"));
        }
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }
}