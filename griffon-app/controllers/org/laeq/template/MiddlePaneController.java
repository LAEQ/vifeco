package org.laeq.template;

import com.sun.media.jfxmedia.MediaException;
import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.core.mvc.MVCGroup;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.DAOException;
import org.laeq.model.Video;
import org.laeq.service.MariaService;
import org.laeq.ui.DialogService;
import org.laeq.video.player.VideoEditor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
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

        list.put("video.create", objects -> {
            File videoFile = (File) objects[0];
                if (videoFile.exists()) {
                    try{
                        Video video = dbService.createVideo(videoFile, Duration.millis(0));
                        MVCGroup group = getApplication().getMvcGroupManager().findGroup("video_container");

                        if(group != null && group.isAlive()){
                            getApplication().getEventRouter().publishEvent("video.created", Arrays.asList(video));
                        } else {
                            createGroup("video_container");
                        }

                    } catch (IOException exception){
                        getLog().error(exception.getMessage());
                        String title = getApplication().getMessageSource().getMessage("org.laeq.title.error");
                        String message = getApplication().getMessageSource().getMessage("org.laeq.video.file.error");
                        dialogService.simpleAlert(title, message);

                    } catch (SQLException | DAOException e) {
                        getLog().error(e.getMessage());
                        String title = getApplication().getMessageSource().getMessage("org.laeq.title.error");
                        String message = getApplication().getMessageSource().getMessage("org.laeq.video.video_dao.error");
                        dialogService.simpleAlert(title, message);
                    } catch (MediaException | javafx.scene.media.MediaException e) {
                        getLog().error(e.getMessage());
                        String title = getApplication().getMessageSource().getMessage("org.laeq.title.error");
                        String message = getApplication().getMessageSource().getMessage("org.laeq.video.media_file.error");
                        dialogService.simpleAlert(title, message);
                    } catch (Exception e){
                        getLog().error(e.getMessage());
                        String title = getApplication().getMessageSource().getMessage("org.laeq.title.error");
                        String message = getApplication().getMessageSource().getMessage("org.laeq.video.file.error");
                        dialogService.simpleAlert(title, message);
                    }

                } else {
                    getLog().error(String.format("PlayerView: file not exits %s", videoFile));
                    String title = getApplication().getMessageSource().getMessage("org.laeq.title.error");
                    dialogService.simpleAlert(title, String.format("PlayerView: file not exits %s", videoFile));
                }
        });

        list.put("video.edit", objects -> {
            Video video = (Video) objects[0];

            VideoEditor editor = null;
            try {
                editor = new VideoEditor(video, dbService.getPointDAO());
                Map<String, Object> args = new HashMap<>();
                args.put("editor", editor);
                createGroup("player", args);
            } catch (IOException e) {
                getLog().error(e.getMessage());
            }
        });

        return list;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void createGroup(String groupName, Map<String, Object> args){
        try{
            createMVCGroup(groupName, args);
        } catch (Exception e){
            getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void createGroup(String groupName){
        try{
            createMVCGroup(groupName);
        } catch (Exception e){
            getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
        }
    }
}