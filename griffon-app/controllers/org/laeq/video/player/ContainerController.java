package org.laeq.video.player;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.service.MariaService;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends AbstractGriffonController {
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private Video video;

    @Inject private MariaService dbService;
    @Inject private DialogService dialogService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        if(video != null){
            Map<String, Object> datas = new HashMap<>();
            datas.put("video", video);

            runInsideUISync(() ->{
                createGroup("controls");
                createGroup("category", datas);
                createGroup("video_player", datas);
                createGroup("timeline", datas);
            });

        } else {
            getLog().error("PlayerController: video is null ??" );
        }
    }

    @Override
    public void mvcGroupDestroy(){
        destroyMVCGroup("controls");
        destroyMVCGroup("category");
        destroyMVCGroup("video_player");
        destroyMVCGroup("timeline");
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
        Set<Category> categorySet = dbService.getCategoryDAO().findByCollection(video.getCollection());

        video.getCollection().getCategorySet().addAll(categorySet);
    }

    private void getPoints(Video video){
        Set<Point> pointSet = dbService.getPointDAO().findByVideo(video);

        video.getPointSet().addAll(pointSet);
    }
}