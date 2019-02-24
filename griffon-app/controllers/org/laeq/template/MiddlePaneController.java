package org.laeq.template;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.DAOException;
import org.laeq.db.DatabaseService;
import org.laeq.model.Video;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class MiddlePaneController extends AbstractGriffonController {
    @MVCMember @Nonnull private MiddlePaneModel model;
    @MVCMember @Nonnull private MiddlePaneView view;
    @Inject private DatabaseService dbService;
    @Inject private DialogService dialogService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listenerList());
        getApplication().getEventRouter().publishEvent("database.user.list");
    }

    private Map<String, RunnableWithArgs> listenerList(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("user.section", objects -> createGroup("user_container"));
        list.put("category.section", objects -> createGroup("category_container"));
        list.put("collection.section", objects -> createGroup("collection_container"));
        list.put("video.section", objects -> createGroup("video_container"));

        list.put("video.add", objects -> {

            File videoFile = (File) objects[0];
            getLog().info(videoFile.toString());

            try {
                Video video = dbService.createVideo(videoFile);

                Map<String, Object> args = new HashMap<>();
                args.put("video", video);

                createGroup("player", args);

            } catch (SQLException | IOException | DAOException e){
                String message = String.format("Error createing the video: %s", videoFile.toString());
                dialogService.simpleAlert(message);
                getLog().error("Error creating and saving video in db: ", videoFile);
            }
        });

        list.put("video.edit", objects -> {
            Video video = (Video) objects[0];

            Map<String, Object> args = new HashMap<>();
            args.put("video", video);

            createGroup("player", args);
        });

        return list;
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void createGroup(String groupName, Map<String, Object> args){
        destroyMVCGroup("player");


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
}