package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Collection;
import org.laeq.model.Point;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            List<Video> videoList = dbService.videoDAO.findAll().stream()
                    .filter(video -> video.getCollection().getId() == collection.getId())
                    .collect(Collectors.toList());

            for(Video video : videoList){
                video.getPoints().forEach(point -> {
                    if(! collection.getCategories().contains(point.getCategory())) {
                        dbService.pointDAO.delete(point);
                    }
                });
            }

            if(dbService.collectionDAO.create(collection)){
                if(model.collections.contains(collection) == false){
                    model.collections.add(collection);
                }
                getApplication().getEventRouter().publishEventOutsideUI("status.success.parametrized", Arrays.asList("db.collection.save.success", collection.getName()));

                view.refresh();
                model.clear();
            } else {
                getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.collection.save.error"));
            }
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.collection.save.error"));
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

       if(dbService.collectionDAO.delete(collection)){
           model.collections.remove(collection);

           getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("db.collection.delete.success", collection.getName()));

           model.clear();
           view.refresh();
        } else {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.collection.delete.error"));
        }
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }
}