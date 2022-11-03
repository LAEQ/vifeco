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
import org.laeq.model.Video;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.util.*;

@ArtifactProviderFor(GriffonController.class)
public class EditorController extends AbstractGriffonController {
    @MVCMember @Nonnull private EditorModel model;
    @MVCMember @Nonnull private EditorView view;
    @MVCMember @Nonnull private Video video;

    @Inject DatabaseService dbService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void togglePlay() {
       getApplication().getEventRouter().publishEventOutsideUI("media.toggle");
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void controls() {
        Stage display = (Stage) getApplication().getWindowManager().findWindow("controls");
        if(display != null){
           return;
        }

        Map<String, Object> args = new HashMap<>();
        args.put("controls", model.controls);
        createMVCGroup("controls", args);
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void imageControls() {
        Stage display = (Stage) getApplication().getWindowManager().findWindow("image_controls");
        if(display != null){
            return;
        }

        Map<String, Object> args = new HashMap<>();
        args.put("controls", model.imageControls);
        createMVCGroup("image_controls", args);
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void detach() {
        Stage display = (Stage) getApplication().getWindowManager().findWindow("detach");
        if(display != null){
            return;
        }

        Map<String, Object> args = new HashMap<>();
        args.put("model", model);
        args.put("controls", model.controls);

        createMVCGroup("full_screen", args);
        getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("video.create.start"));
    }

    @Override
    public void mvcGroupDestroy(){

    }

    @ControllerAction
    public void addPoint(KeyCode code) {
        getApplication().getEventRouter().publishEvent("point.adding", Arrays.asList(code));
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void add() {
        model.isReady.set(Boolean.FALSE);

        getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("display"));

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
            args.put("controls", model.controls);

            createMVCGroup("display", args);
            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("video.create.start"));
        } else {
            model.isReady.set(Boolean.TRUE);
        }
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

//        list.put("display.ready", objects -> model.isReady.set(Boolean.TRUE));
//        list.put("icon.delete", objects -> deletePoint((IconPointColorized) objects[0]));

        return list;
    }

    public EditorController() {
        super();
    }

    public void draw() {
        Stage display = (Stage) getApplication().getWindowManager().findWindow("drawing");
        if(display != null){
            return;
        }

        createMVCGroup("drawing");
    }
}