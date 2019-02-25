package org.laeq.video;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.CRUDController;
import org.laeq.db.*;
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

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        videoDAO = dbService.getVideDAO();
        ccDAO = dbService.getCategoryCollectionDAO();
        userDAO = dbService.getUserDAO();

        System.out.println(videoDAO.findAll().size());
        model.getVideoList().addAll(videoDAO.findAll());
        model.getUserSet().addAll(userDAO.findAll());
        model.getCollectionSet().addAll(ccDAO.findAll());

        view.initForm();
    }
    public void save(){
        if(model.valid()){
            try {
                Video video = model.generateVideo();
                videoDAO.update(video);
                runInsideUISync(() -> {
                    model.update(video);
                    model.clear();
                    view.resetComboBox();
                });

            } catch (SQLException | DAOException e) {
                alert("key.to_implement", e.getMessage());
            }
        } else {
            alert("org.laeq.video.validation.title_error", model.getErrors());
        }
    }

    public void clear(){
        view.resetComboBox();
    }

    public void editVideo(Video video) {
        publishEvent("video.edit", video);
    }

    private void publishEvent(String eventName, Video video){
        getApplication().getEventRouter().publishEventOutsideUI(eventName, Arrays.asList(video));
    }
}
