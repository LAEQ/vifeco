package org.laeq.editor;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import griffon.transform.Threading;
import org.laeq.DatabaseService;
import org.laeq.model.Point;
import org.laeq.model.Video;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class PlayerController extends AbstractGriffonController {
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private PlayerView view;
    @MVCMember @Nonnull private Video video;

    @Inject DatabaseService dbService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {

    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void stop() {
        getApplication().getEventRouter().publishEventAsync("player.pause");
        view.pause();

    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void play() {
        getApplication().getEventRouter().publishEventAsync("player.play");
        view.play();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void controls() {
        createMVCGroup("test2");
    }

    @Override
    public void mvcGroupDestroy(){
        System.out.println("destroying player");
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void addPoint(KeyCode code, Duration currentTime) {
        if(model.enabled){
            Point point = model.generatePoint(code.getName(), currentTime);

            if(point != null){
                try {
                    dbService.pointDAO.create(point);
                    model.addPoint(point);
                    getApplication().getEventRouter().publishEventOutsideUI("status.success.parametrized", Arrays.asList("editor.point.create.success", point.toString()));
                    getApplication().getEventRouter().publishEventOutsideUI("point.created");
                } catch (Exception e) {
                    getApplication().getEventRouter().publishEvent("status.error.parametrized", Arrays.asList("editor.point.create.error", point.toString()));
                }
            }
        }
    }
    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void deletePoint(Point point) {
        try{
            dbService.pointDAO.delete(point);
            model.removePoint(point);
            view.refresh();
            getApplication().getEventRouter().publishEventOutsideUI("status.success.parametrized", Arrays.asList("editor.point.delete.success", point.toString()));
            getApplication().getEventRouter().publishEventOutsideUI("point.deleted");
        }catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error.parametrized", Arrays.asList("editor.point.delete.error", point.toString()));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void add() {
        System.out.println("ICFDFSDDSFF");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Video Files",
                        "*.mp4", "*.wav", "*.mkv", "*.avi", "*.wmv", "*.mov")
        );

        Stage stage = (Stage) getApplication().getWindowManager().findWindow("test");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("video.create.start"));

            Map<String, Object> args = new HashMap<>();
            args.put("file", selectedFile);
            createMVCGroup("display", args);

        } else {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.create.error"));
        }
    }
}