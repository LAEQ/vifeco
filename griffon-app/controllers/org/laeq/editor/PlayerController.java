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
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPointColorized;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    @Override
    public void mvcGroupDestroy(){
        view.pause();
        getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("display"));
        getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("controls"));
    }

    @ControllerAction
    public void addPoint(KeyCode code, Duration currentTime) {
        if(model.isReady.get()){
            runInsideUIAsync(() -> {
                Point point = model.generatePoint(code.getName(), currentTime);

                if(point == null){
                    return;
                }

                if(dbService.pointDAO.create(point)){
                    model.addPoint(point);
                    view.addPoint(point);

                    getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("editor.point.create.success", point.toString()));
                    getApplication().getEventRouter().publishEvent("point.added", Arrays.asList(point));
                } else {
                    getApplication().getEventRouter().publishEvent("status.error.parametrized", Arrays.asList("editor.point.create.error", point.toString()));
                }
            });
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void deletePoint(Point point) {
        runInsideUISync(() -> {
            model.removePoint(point);
            view.removePoint(point);
            if(dbService.pointDAO.delete(point)){
                getApplication().getEventRouter().publishEventOutsideUI("status.success.parametrized", Arrays.asList("editor.point.delete.success", point.toString()));
                getApplication().getEventRouter().publishEventOutsideUI("point.deleted", Arrays.asList(point));
            } else {
                getApplication().getEventRouter().publishEvent("status.error.parametrized", Arrays.asList("editor.point.delete.error", point.toString()));
            }
        });

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

        list.put("display.ready", objects -> {
           model.isReady.set(Boolean.TRUE);
        });

        list.put("speed.change", objects -> {
            view.refreshRate((Double) objects[0]);
            getApplication().getEventRouter().publishEvent("video.currentTime", Arrays.asList(view.getCurrentTime()));
        });

        list.put("opacity.change", objects -> {
            view.refreshOpacity((Double) objects[0]);
        });
        list.put("duration.change", objects -> {
            view.setDuration(model.controls.display());
        });
        list.put("size.change", objects -> {
            view.refreshSize((Double) objects[0]);
        });
        list.put("volume.change", objects -> {
            view.refreshVolume((Double) objects[0]);
        });

        list.put("row.currentTime", objects -> {
            view.rewind((Duration) objects[0]);
        });

        list.put("point.delete", objects -> {
            deletePoint((Point) objects[0]);
        });
        list.put("icon.delete", objects -> {
            deletePoint((IconPointColorized) objects[0]);
        });

        list.put("slider.release", objects -> {
            view.sliderReleased(video.getDuration().multiply((Double) objects[0] / 100));
        });
        list.put("slider.pressed", objects -> {
            view.sliderPressed();
        });
        list.put("slider.currentTime", objects -> {
            Duration now = video.getDuration().multiply((Double) objects[0] / 100);
            view.sliderCurrentTime(now);
        });

        list.put("mouse.position", objects -> {
            model.setMousePosition((Point2D) objects[0]);
        });

        list.put("player.forward.5", objects -> {
            Duration now = view.getCurrentTime().add(Duration.seconds(5));
            if(now.greaterThan(video.getDuration())){
                now = video.getDuration();
            }

            view.rewind(now);
            getApplication().getEventRouter().publishEventOutsideUI("player.rewind", Arrays.asList(now));
        });
        list.put("player.rewind.5", objects -> {
            Duration now = view.getCurrentTime().subtract(Duration.seconds(5));
            if(now.lessThan(Duration.ZERO)){
                now = Duration.ZERO;
            }

            view.rewind(now);
            getApplication().getEventRouter().publishEventOutsideUI("player.rewind", Arrays.asList(now));
        });

        list.put("elapsed.focus.on", objects -> {
            stop();
        });
        list.put("elapsed.currentTime", objects -> {
            view.rewind((Duration) objects[0]);
        });

        return list;
    }

    public void deletePoint(IconPointColorized icon) {
        if(icon == null){
            return;
        }

        Optional<Point> point = model.getPointFromIcon(icon);

        if(point.isPresent()){
            deletePoint(point.get());
        }
    }
}