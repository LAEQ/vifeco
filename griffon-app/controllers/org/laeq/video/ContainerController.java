package org.laeq.video;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.control.TableColumn;
import org.laeq.CRUDController;
import org.laeq.db.*;
import org.laeq.model.CategoryCollection;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends CRUDController<Video> {
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private ContainerView view;

    @Inject private DatabaseService dbService;
    @Inject private DialogService dialogService;

    private VideoDAO videoDAO;
    private UserDAO userDAO;
    private CategoryCollectionDAO ccDAO;
    private CategoryDAO categoryDAO;
    private PointDAO pointDAO;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        videoDAO = dbService.getVideDAO();
        ccDAO = dbService.getCategoryCollectionDAO();
        userDAO = dbService.getUserDAO();
        categoryDAO = dbService.getCategoryDAO();
        pointDAO = dbService.getPointDAO();

        System.out.println(videoDAO.findAll().size());
        model.getVideoList().addAll(videoDAO.findAll());
        model.getUserSet().addAll(userDAO.findAll());
        model.getCollectionSet().addAll(ccDAO.findAll());
        model.addCategories(categoryDAO.findAll());


        view.initForm();
    }

    public void clear(){
        view.reset();
    }

    public void edit(){
        publishEvent("video.edit", this.model.getSelectedVideo());
    }

    public void editVideo(Video video) {
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

    public void updateCollection(TableColumn.CellEditEvent<Video, CategoryCollection> event) {
        try {
            videoDAO.updateCollection(event.getRowValue(), event.getNewValue());
            event.getRowValue().setCategoryCollection(event.getNewValue());
        } catch (SQLException | DAOException e) {
            alert("key.to_implement", e.getMessage());
        }
    }

    public void showDetail() {
        model.getSelectedVideo().getPointSet().addAll(pointDAO.findByVideo(model.getSelectedVideo()));
        model.getSelectedVideo().getCategoryCollection().getCategorySet().addAll(categoryDAO.findByCollection(model.getSelectedVideo().getCategoryCollection()));
        view.showDetails();

    }
}
