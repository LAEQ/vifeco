package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class VideoController extends AbstractGriffonController {
    @MVCMember @Nonnull private VideoModel model;
    @MVCMember @Nonnull private VideoView view;

    @Inject private DatabaseService dbService;
    @Inject private ExportService exportService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try{
            model.videoList.addAll(dbService.videoDAO.findAll());
            model.getUserSet().addAll(dbService.userDAO.findAll());
            model.getCollectionSet().addAll(dbService.collectionDAO.findAll());
            model.categorySet.addAll(dbService.categoryDAO.findAll());
            getApplication().getEventRouter().publishEventOutsideUI("status.info", Arrays.asList("db.success.fetch"));

            model.videoList.forEach(v -> setVideoDuration(v));

            //@todo add BiDirectionalBinding to remove this hack
            view.initForm();
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.fetch"));
        }

        getApplication().getEventRouter().addEventListener(listeners());
    }

    private void setVideoDuration(Video video){
        if(video.getDuration().equals(Duration.UNKNOWN) == false){
            return;
        }
        runOutsideUI(() -> {
            File file = new File(video.getPath());

            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    video.setDuration(media.getDuration());
                    try {
                        dbService.videoDAO.create(video);
                        getApplication().getEventRouter().publishEvent("videolist.refresh");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            mediaPlayer.setOnError(new Runnable() {
                @Override
                public void run() {
                    System.out.println("ERROR");
                }
            });
        });

    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void clear(){
        model.clear();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void delete(Video video){
        try{
            dbService.videoDAO.delete(video);
            model.videoList.remove(video);
            getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("video.delete.success", video.pathToName()));
        }  catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.delete.error"));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void edit(){
        if(model.selectedVideo == null){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.edit.error"));
        } else {
            getApplication().getEventRouter().publishEvent("status.info.parametrized", Arrays.asList("video.edit.success",model.selectedVideo.getPath()));
            createDisplay();
        }
    }

    private void  createDisplay(){
        if(model.currentVideo != null){
            destroyMVCGroup(model.currentVideo);
        }

        model.currentVideo = "currentVideo";

        Map<String, Object> args = new HashMap<>();
        args.put("video",model.selectedVideo);
        createMVCGroup("currentVideo", args);
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD_ASYNC)
    public void export(Video video){
        try {
            String filename = exportService.export(video);

            getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("video.export.success", filename));
        } catch (IOException e) {
            e.printStackTrace();
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.export.error"));
        }
    }


    public void updateUser(TableColumn.CellEditEvent<Video, User> event) {
        try {
            Video video = event.getRowValue();
            video.setUser(event.getNewValue());
            dbService.videoDAO.create(video);
            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("video.user.updated.success"));
        } catch (Exception e) {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.user.updated.error"));
        }

        runInsideUISync(() -> {
            view.refresh();
        });
    }

    public void updateCollection(Video video, Collection newValue) {
        try{
            clear();
            video.updateCollection(newValue);
            dbService.videoDAO.create(video);

            getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("video.collection.updated.success"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.collection.updated.error"));
        }

        runInsideUISync(() -> {
            view.refresh();
        });
    }

    public void select(Video video) {
        runInsideUISync(() -> {
            model.clear();
            model.setSelectedVideo(video);
        });
    }

    private void refreshVideoList(){
        try {
            model.videoList.clear();
            model.videoList.addAll(dbService.videoDAO.findAll());
            model.videoList.forEach(v -> setVideoDuration(v));
        } catch (Exception e) {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.fetch"));
        }
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("video.created", objects -> refreshVideoList());
        list.put("videolist.refresh", objects -> view.refresh());

        list.put("point.created", objects -> {
            runInsideUISync(() -> {
                view.refresh();
            });
        });

        return list;
    }
}
