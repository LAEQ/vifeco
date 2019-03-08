package org.laeq.template;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.DAOException;
import org.laeq.model.Video;
import org.laeq.service.MariaService;
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
    @Inject private MariaService dbService;
    @Inject private DialogService dialogService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listenerList());
    }

    private Map<String, RunnableWithArgs> listenerList(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("user.section", objects -> createGroup("user_container"));
        list.put("category.section", objects -> createGroup("category_container"));
        list.put("collection.section", objects -> createGroup("collection_container"));
        list.put("video.section", objects -> createGroup("video_container"));
        list.put("database.section", objects -> createGroup("status"));

        list.put("video.add", objects -> {
            File videoFile = (File) objects[0];
                if (videoFile.exists()) {
//                    try {
//                        Media media = new Media(videoFile.getCanonicalFile().toURI().toString());
//                        MediaPlayer mediaPlayer = new MediaPlayer(media);
//                        mediaPlayer.setOnReady(() -> {
//                        Duration duration = mediaPlayer.getMedia().getDuration();

                            try {
                                Video video = dbService.createVideo(videoFile, Duration.millis(10));
                                Map<String, Object> args = new HashMap<>();
                                args.put("video", video);

                                createGroup("player", args);
                            } catch (IOException | SQLException | DAOException e) {
                                String message = String.format("Error createing the video: %s", videoFile.toString());
                                dialogService.simpleAlert("key.to_implement", message);
                                getLog().error("Error creating and saving video in db: ", videoFile);
                            }
//                        });

//                    } catch (IOException e) {
//                        getLog().error(String.format("Create video with file %s", videoFile));
//                    }
                } else {
                    getLog().error(String.format("PlayerView: file not exits %s", videoFile));
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


    @ControllerAction
    @Threading(Threading.Policy.SKIP)
    public void createGroup(String groupName, Map<String, Object> args){
        try{
            createMVCGroup(groupName, args);
        } catch (Exception e){
            getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.SKIP)
    public void createGroup(String groupName){
        try{
            createMVCGroup(groupName);
        } catch (Exception e){
            getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
        }
    }
}