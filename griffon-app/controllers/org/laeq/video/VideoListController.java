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
            createGroup("category_create");
        });

        result.put("mvc.category.list", objects -> {
            createGroup("category_list");
        });

        result.put("org.laeq.user.create", objects -> {
            createGroup("user_create");
        });

        result.put("org.laeq.user.list", objects -> {
            createGroup("user_list");
        });

        result.put("video.list", objects -> {
           createGroup("video_list");
        });

        result.put("video.player", objects -> {
            createGroup("video_player");
        });
        
        return result;
    }

    public void createGroup(String groupName){
        runInsideUISync(() -> {
            try{
                createMVCGroup(groupName);
            } catch (Exception e){
                getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
            }
        });
    }
}