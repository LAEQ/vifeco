package org.laeq.editor;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.DatabaseService;
import org.laeq.model.Drawing;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPointColorized;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.util.*;

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
        model.isPlaying.set(false);
        view.pause();
        getApplication().getEventRouter().publishEventOutsideUI("player.pause");
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void play() {
        if(model.isReady.get()){
            model.isPlaying.set(true);
            getApplication().getEventRouter().publishEventOutsideUI("player.currentTime", Arrays.asList(view.getCurrentTime()));
            getApplication().getEventRouter().publishEventOutsideUI("player.play");
            view.play();
        }
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
        args.put("currentTime", view.getCurrentTime());
        args.put("controls", model.controls);

        createMVCGroup("full_screen", args);
        getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("video.create.start"));
    }

    @Override
    public void mvcGroupDestroy(){
        view.pause();
        getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("display"));
        getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("controls"));
        getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("image_controls"));
        getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("drawing"));
    }

    @ControllerAction
    public void addPoint(KeyCode code, Duration currentTime) {
        getApplication().getEventRouter().publishEventOutsideUI("point.adding", Arrays.asList(code, currentTime));
//        if(model.isReady.get()){
//            runInsideUIAsync(() -> {
//                final Point point = model.generatePoint(code.getName(), currentTime);
//
//                if(point == null){
//                    return;
//                }
//
//                if(dbService.pointDAO.create(point)){
//                    model.addPoint(point);
//                    view.addPoint(point);
//
//                    getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("editor.point.create.success", point.toString()));
//                    getApplication().getEventRouter().publishEvent("point.added", Arrays.asList(point));
//                } else {
//                    getApplication().getEventRouter().publishEvent("status.error.parametrized", Arrays.asList("editor.point.create.error", point.toString()));
//                }
//            });
//        }
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void rewind(){
        Duration now = view.getCurrentTime().subtract(Duration.seconds(30));
        if(now.lessThan(Duration.ZERO)){
            now = Duration.ZERO;
        }

        getApplication().getEventRouter().publishEvent("player.rewind", Arrays.asList(now));
        view.rewind(now);
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void forward(){
        Duration now = view.getCurrentTime().add(Duration.seconds(30));
        if(now.greaterThan(video.getDuration())){
           now = video.getDuration();
        }

        getApplication().getEventRouter().publishEvent("player.rewind", Arrays.asList(now));
        view.rewind(now);
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void add() {
        view.pause();
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
            args.put("currentTime", view.getCurrentTime());
            args.put("controls", model.controls);

            createMVCGroup("display", args);
            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("video.create.start"));
        } else {
            model.isReady.set(Boolean.TRUE);
        }
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("display.ready", objects -> model.isReady.set(Boolean.TRUE));

        list.put("speed.change", objects -> {
            view.refreshRate((Double) objects[0]);
            getApplication().getEventRouter().publishEvent("video.currentTime", Arrays.asList(view.getCurrentTime()));
        });

        list.put("brightness.change", objects -> view.refreshBrightness((Double) objects[0]));
        list.put("saturation.change", objects -> view.refreshSaturation((Double) objects[0]));
        list.put("constrast.change", objects -> view.refreshContrast((Double) objects[0]));
        list.put("hue.change", objects -> view.refreshHue((Double) objects[0]));


        list.put("volume.change", objects -> view.refreshVolume((Double) objects[0]));

        list.put("row.currentTime", objects -> view.rewind((Duration) objects[0]));

//        list.put("timeline.point.deleted", objects -> {
//            Point pt = (Point) objects[0];
//            model.removePoint(pt);
//            view.removePoint(pt);
//        });

        list.put("icon.delete", objects -> deletePoint((IconPointColorized) objects[0]));

        list.put("slider.release", objects -> view.sliderReleased(video.getDuration().multiply((Double) objects[0] / 100)));
        list.put("slider.pressed", objects -> view.sliderPressed());
        list.put("slider.currentTime", objects -> {
            Duration now = video.getDuration().multiply((Double) objects[0] / 100);
            view.sliderCurrentTime(now);
        });

        list.put("mouse.position", objects -> model.setMousePosition((Point2D) objects[0]));
        list.put("mouse.active", objects -> model.setMouseActive((boolean) objects[0]));

        list.put("elapsed.focus.on", objects -> stop());
        list.put("elapsed.currentTime", objects -> view.rewind((Duration) objects[0]));

        list.put("drawing.line.start", args -> view.drawLineStart((String) args[0]));
        list.put("drawing.rectangle.start", args -> view.drawRectangleStart((String) args[0]));
        list.put("drawing.updated", args -> view.drawingUpdated((List<Drawing>) args[0]));
        list.put("drawing.destroyed", args -> view.drawingDestroy());

        return list;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void deletePoint(IconPointColorized icon) {
        runInsideUIAsync(() -> {
            Optional<Point> point = model.getPointFromIcon(icon);
            if(point.isPresent() && dbService.pointDAO.delete(point.get())) {
                Point pt = point.get();
                getApplication().getEventRouter().publishEvent("player.point.deleted", Arrays.asList(pt));

                model.removePoint(pt);
                view.removePoint(pt);
            }
        });
    }

    public void rewind(double value) {
        final Duration now = view.getCurrentTime().subtract(Duration.seconds(value));
        if(now.lessThan(Duration.ZERO)){
            view.rewind(Duration.ZERO);
            getApplication().getEventRouter().publishEventOutsideUI("player.rewind", Arrays.asList(Duration.ZERO));
        } else{
            view.rewind(now);
            getApplication().getEventRouter().publishEventOutsideUI("player.rewind", Arrays.asList(now));
        }
    }

    public void forward(double value) {
        final Duration now = view.getCurrentTime().add(Duration.seconds(value));
        if(now.greaterThan(video.getDuration()) == false){
            view.rewind(now);
            getApplication().getEventRouter().publishEventOutsideUI("player.rewind", Arrays.asList(now));
        }
    }

    public PlayerController() {
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