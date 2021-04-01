package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Collection;
import org.laeq.model.Video;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class CollectionController extends AbstractGriffonController implements CRUDInterface<Collection> {
    @MVCMember @Nonnull private CollectionModel model;
    @MVCMember @Nonnull private CollectionView view;
    @Inject private DatabaseService dbService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try {
            model.collections.addAll(dbService.collectionDAO.findAll());
            model.addCategories(dbService.categoryDAO.findAll());
            model.categories.addAll(dbService.categoryDAO.findAll());
            getApplication().getEventRouter().publishEventOutsideUI("status.info", Arrays.asList("db.collection.fetch.success"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.collection.fetch.error"));
        }

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    @Override
    public void save(){
        try{
            Collection collection = model.getCollection();
            List<Video> videoList = dbService.videoDAO.findAll();

            videoList.stream().filter(video -> video.getCollection().getId().equals(collection.getId())).forEach(video -> {
                video.getPoints().stream()
                        .filter(point -> collection.getCategories().contains(point.getCategory()) == false)
                        .forEach(point ->
                                {
                                    try {
                                        dbService.pointDAO.delete(point);
                                    } catch (Exception e) {
                                        getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.collection.save.error"));
                                    }
                                }
                        );

            });

            dbService.collectionDAO.create(collection);
            model.collections.clear();
            model.collections.addAll(dbService.collectionDAO.findAll());
            getApplication().getEventRouter().publishEventOutsideUI("status.success.parametrized", Arrays.asList("db.collection.save.success", collection.getName()));
        }catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.collection.save.error"));
        }finally {
            model.clear();
        }
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    @Override
    public void clear(){
        model.clear();
        getApplication().getEventRouter().publishEvent("status.reset");
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    @Override
    public void delete(Collection collection) {
        if(collection.getDefault()){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.collection.delete.default"));
            return;
        }

        try {
            dbService.collectionDAO.delete(collection);
            model.collections.clear();
            model.collections.addAll(dbService.collectionDAO.findAll());
            getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("db.collection.delete.success", collection.getName()));
        } catch (Exception e) {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.collection.delete.error"));
        }finally {
            model.clear();
        }
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }
}