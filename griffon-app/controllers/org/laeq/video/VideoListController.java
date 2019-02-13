package org.laeq.video;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.DatabaseService;
import org.laeq.model.Video;
import org.laeq.model.VideoUser;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.util.*;

@ArtifactProviderFor(GriffonController.class)
public class VideoListController extends AbstractGriffonController {
    @MVCMember @Nonnull private VideoListModel model;
    @MVCMember @Nonnull private VideoListView view;
    @Inject private DatabaseService service;
    @Inject private DialogService dialogService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());

//        getApplication().getEventRouter().addEventListener("database.video.created", videos -> {
//            Video video = (Video) videos[0];
//            runInsideUISync(() -> {
//                this.model.addVideo(video);
//            });
//        });
    }

    public void exportVideo(VideoUser videoUser) {
        String text = String.format("You are about to export: %s", videoUser.getVideo().getName());
        dialogService.dialog(text);
    }

    public void deleteVideo(VideoUser videoUser) {
        String text = String.format("Attention ! \n \n You are about to delete: %s", videoUser.getVideo().getName());
        dialogService.dialog(text);
    }

    public void editVideo(VideoUser videoUser) {
        getApplication().getEventRouter().publishEvent("database.video_user.load", Arrays.asList(videoUser));
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> result = new HashMap<>();

        result.put("database.video_user.findAll", objects -> {
            System.out.println("database.video_user.findAll");

            runInsideUISync(()->{
                this.model.addVideos((List<VideoUser>) objects[0]);
            });
        });

        result.put("database.video_user.created", objects -> {
            runInsideUISync(()->{

            });
        });

        return result;
    }
}