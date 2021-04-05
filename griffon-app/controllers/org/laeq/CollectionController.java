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
import java.util.concurrent.atomic.AtomicReference;

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

            AtomicReference<Boolean> result = new AtomicReference<>(Boolean.TRUE);

            videoList.stream().filter(video -> video.getCollection().getId().equals(collection.getId())).forEach(video -> {
                video.getPoints().stream()
                        .filter(point -> collection.getCategories().contains(point.getCategory()) == false)
                        .forEach(point -> {
                                   if(dbService.pointDAO.delete(point) == false){
                                       result.set(Boolean.FALSE);
                                   }
                        }
                );
            });

            if(result.get() && dbService.collectionDAO.create(collection)){
                if(model.collections.contains(collection) == false){
                    model.collections.add(collection);
                }
                view.refresh();
                model.clear();
                getApplication().getEventRouter().publishEventOutsideUI("status.success.parametrized", Arrays.asList("db.collection.save.success", collection.getName()));
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
           if(model.collections.contains(collection) == false){
               model.collections.add(collection);
           }
           view.refresh();
           getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("db.collection.delete.success", collection.getName()));
        } else {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.collection.delete.error"));
        }
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }
}