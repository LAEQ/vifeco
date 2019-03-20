package org.laeq.video;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.scene.control.TableColumn;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.laeq.CRUDController;
import org.laeq.db.*;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.service.MariaService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends CRUDController<Video> {
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private ContainerView view;

    @Inject private MariaService dbService;
    @Inject private ExportService exportService;

    private VideoDAO videoDAO;
    private UserDAO userDAO;
    private CollectionDAO ccDAO;
    private CategoryDAO categoryDAO;
    private PointDAO pointDAO;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        videoDAO = dbService.getVideoDAO();
        ccDAO = dbService.getCollectionDAO();
        userDAO = dbService.getUserDAO();
        categoryDAO = dbService.getCategoryDAO();
        pointDAO = dbService.getPointDAO();

        model.getVideoList().addAll(videoDAO.findAll());
        model.getUserSet().addAll(userDAO.findAll());
        model.getCollectionSet().addAll(ccDAO.findAll());
        model.addCategories(categoryDAO.findAll());

        view.initForm();

        model.getVideoList().forEach(video -> {
            if(video.getDuration() == 0.0){
                runInsideUISync(() ->getVideoDuration(video));
            }
        });

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    private void getVideoDuration(Video video) {
        File file = new File(video.getPath());

        if (file.exists()) {
            try {
                Media media = new Media(file.getCanonicalFile().toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);

                mediaPlayer.setOnReady(()-> {
                    video.setDuration(mediaPlayer.getMedia().getDuration().toMillis());
                    try {
                        videoDAO.updateDuration(video);
                    } catch (SQLException | DAOException e) {
                        getLog().error(e.getMessage());
                    }
                });
            } catch (IOException e) {
                getLog().error(e.getMessage());
            }
        }
    }

    public void clear(){
        view.reset();
    }

    public void delete(){

        if(model.getSelectedVideo() == null){
            alert("key.to_implement", "org.laeq.video.no_selection");
            return;
        }

        runInsideUISync(() -> {
            Boolean confirmation = confirm("org.laeq.video.delete.confirm");
            if(confirmation){
                try {
                    videoDAO.delete(model.getSelectedVideo());
                    model.deleteVideo();
                    view.reset();
                } catch (DAOException e) {
                    getLog().error(e.getMessage());
                }
            }
        });
    }

    public void edit(){
        if(model.getSelectedVideo() == null){
            alert("key.to_implement", getMessage("org.laeq.video.no_selection"));
            return;
        }

        publishEvent("video.edit", this.model.getSelectedVideo());
    }

    public void export(){
        if(model.getSelectedVideo() == null){
            alert("key.to_implement", getMessage("org.laeq.video.no_selection"));
            return;
        }

        exportService.export(model.getSelectedVideo());
    }

    public void editVideo(Video video) {
        if(model.getSelectedVideo() == null){
            alert("key.to_implement", getMessage("org.laeq.video.no_selection"));
            return;
        }

        publishEvent("video.edit", video);
    }

    private void publishEvent(String eventName, Video video){
        getApplication().getEventRouter().publishEventOutsideUI(eventName, Arrays.asList(video));
    }

    public void updateUser(TableColumn.CellEditEvent<Video, User> event) {
        try {
            videoDAO.updateUser(event.getRowValue(), event.getNewValue());
            event.getRowValue().setUser(event.getNewValue());
        } catch (SQLException | DAOException e) {
            alert("key.to_implement", e.getMessage());
        }
    }

    public void updateCollection(TableColumn.CellEditEvent<Video, Collection> event) {
        try {
            videoDAO.updateCollection(event.getRowValue(), event.getNewValue());
            event.getRowValue().setCollection(event.getNewValue());
        } catch (SQLException | DAOException e) {
            alert("key.to_implement", e.getMessage());
        }
    }

    public void showDetail() {
        if(model.getSelectedVideo() != null){
            model.getSelectedVideo().getPointSet().addAll(pointDAO.findByVideo(model.getSelectedVideo()));
            model.getSelectedVideo().getCollection().getCategorySet().addAll(categoryDAO.findByCollection(model.getSelectedVideo().getCollection()));
            view.showDetails();
        }
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("video.import.success", objects -> {
            model.getVideoList().clear();
            model.getVideoList().addAll(videoDAO.findAll());
        });

        list.put("video.created", objects -> {
            Video video = (Video) objects[0];
            runInsideUISync(() -> {
                model.getVideoList().add(video);
                getVideoDuration(video);
            });
        });

        return list;
    }
}
