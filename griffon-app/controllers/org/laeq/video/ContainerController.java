package org.laeq.video;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.DatabaseService;
import org.laeq.db.VideoDAO;
import org.laeq.model.Video;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends AbstractGriffonController {
    @MVCMember @Nonnull private ContainerModel model;

    @Inject private DatabaseService dbService;
    @Inject private DialogService dialogService;

    private VideoDAO videoDAO;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        videoDAO = dbService.getVideDAO();
        model.getVideoList().addAll(videoDAO.findAll());
    }

    public void editVideo(Video video) {
        publishEvent("video.edit", video);
    }

    private void publishEvent(String eventName, Video video){
        getApplication().getEventRouter().publishEventOutsideUI(eventName, Arrays.asList(video));
    }
}
