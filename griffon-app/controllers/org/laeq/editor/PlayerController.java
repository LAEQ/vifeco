package org.laeq.editor;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
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
        getApplication().getEventRouter().addEventListener(listeners());
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void stop() {
        getApplication().getEventRouter().publishEvent("player.pause");
        view.pause();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void play() {
        if(model.isReady.get()){
            getApplication().getEventRouter().publishEvent("player.play");
            getApplication().getEventRouter().publishEventOutsideUI("player.currentTime", Arrays.asList(view.getCurrentTime()));
            view.play();
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void controls() {
        createMVCGroup("controls");
    }

    @Override
    public void mvcGroupDestroy(){
        Stage display = (Stage) getApplication().getWindowManager().findWindow("display");
        if(display != null){
            getApplication().getWindowManager().detach("display");
            display.close();
        }

        System.out.println("C: " + getApplication().getMvcGroupManager().getGroups().keySet());
        System.out.println("C: " + getApplication().getWindowManager().getWindowNames());
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
                    view.refresh();
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
        view.pause();
        model.isReady.set(Boolean.FALSE);

        try {
            Stage window = (Stage) getApplication().getWindowManager().findWindow("display");
            window.close();
            getApplication().getMvcGroupManager().findGroup("display").destroy();
        }catch (Exception e){

        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Video Files",
                        "*.mp4", "*.wav", "*.mkv", "*.avi", "*.wmv", "*.mov")
        );

        Stage stage = (Stage) getApplication().getWindowManager().findWindow("editor");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            Map<String, Object> args = new HashMap<>();
            args.put("file", selectedFile);
            args.put("currentTime", view.getCurrentTime());

            createMVCGroup("display", args);
            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("video.create.start"));
        } else {
            model.isReady.set(Boolean.TRUE);
        }
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("display.ready", objects -> {
           model.isReady.set(Boolean.TRUE);
        });

        return list;
    }

    public void updateCurrentTime(Duration start) {
        getApplication().getEventRouter().publishEvent("player.currentTime", Arrays.asList(start));
    }
}