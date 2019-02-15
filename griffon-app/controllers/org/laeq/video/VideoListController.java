package org.laeq.video;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.DatabaseService;
import org.laeq.model.VideoUser;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class VideoListController extends AbstractGriffonController {
    @MVCMember @Nonnull private VideoListModel model;
    @MVCMember @Nonnull private VideoListView view;
    @Inject private DatabaseService service;
    @Inject private DialogService dialogService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
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
            runInsideUISync(()->{
                this.model.addVideos((List<VideoUser>) objects[0]);
            });
        });

        result.put("database.video_user.created", objects -> {
            runInsideUISync(()->{
                this.model.addVideo((VideoUser) objects[0]);
            });
        });

        result.put("category.create", objects -> {
            String mvcIdentifier = "category.create";
            runInsideUISync(() -> {
                createMVCGroup("category_create");
            });
        });

        return result;
    }
}