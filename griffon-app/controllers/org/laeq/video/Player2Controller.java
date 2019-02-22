package org.laeq.video;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import org.laeq.db.*;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ArtifactProviderFor(GriffonController.class)
public class Player2Controller extends AbstractGriffonController {
    @MVCMember @Nonnull private Player2Model model;
    @MVCMember @Nonnull private Video video;

    @Inject private DatabaseService dbService;
    @Inject private DialogService dialogService;


    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        if(video != null){

            getCategoryList(video);
            getPoints(video);

            Map<String, Object> datas = new HashMap<>();
            datas.put("video", video);

            runInsideUISync(() ->{
                createGroup("controls");
                createGroup("category", datas);
                createGroup("video_player", datas);
            });

        } else {
            getLog().error("PlayerController: video is null ??" );
        }
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void createGroup(String groupName, Map<String, Object> args){
        try{
            createMVCGroup(groupName, args);
        } catch (Exception e){
            getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
        }
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void createGroup(String groupName){
        try{
            createMVCGroup(groupName);
        } catch (Exception e){
            getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
        }
    }

    private void getCategoryList(Video video){
        Set<Category> categorySet = dbService.getCategoryDAO().findByCollection(video.getCategoryCollection());

        video.getCategoryCollection().getCategorySet().addAll(categorySet);
    }

    private void getPoints(Video video){
        Set<Point> pointSet = dbService.getPointDAO().findByVideo(video);

        video.getPointSet().addAll(pointSet);
    }
}