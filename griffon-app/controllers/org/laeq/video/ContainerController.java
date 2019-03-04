package org.laeq.video;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.control.TableColumn;
import org.laeq.CRUDController;
import org.laeq.db.*;
import org.laeq.model.*;
import org.laeq.service.MariaService;
import org.laeq.settings.Settings;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

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
}
