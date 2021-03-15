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
        try{
            model.categoryList.addAll(dbService.categoryDAO.findAll());
            getApplication().getEventRouter().publishEventOutsideUI("status.info", Arrays.asList("db.category.fetch.success"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.category.fetch.error"));
        }

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    @Override
    public void save(){
        try{
            Category category = model.getCategory();
            dbService.categoryDAO.create(category);
            model.categoryList.clear();
            model.categoryList.addAll(dbService.categoryDAO.findAll());
            getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("db.category.save.success", category.getName()));
            model.clear();
        } catch (Exception e){
            model.resetId();
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.category.save.error"));
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
        try{
            dbService.categoryDAO.delete(category);
            model.categoryList.remove(category);
            getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("db.category.delete.success", category.getName()));
        }  catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.category.delete.error"));
        }
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }
}