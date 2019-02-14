package org.laeq.video;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.scene.input.KeyEvent;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.VideoPoint;
import org.laeq.model.VideoUser;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class PlayerController extends AbstractGriffonController {
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private PlayerView view;
    @Inject private DialogService dialogService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listenerList());
    }

    public void dispatchVideoCreated(Video video){
        getApplication().getEventRouter().publishEventAsync("database.model.created", Arrays.asList(video));
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void play() {
        view.play();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void rewind() {
        dialogService.dialog();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void forward() {
        dialogService.dialog();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void backVideo() {
        dialogService.dialog();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void test(KeyEvent keyEvent) {
        System.out.println("test");
    }

    private Map<String, RunnableWithArgs> listenerList(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("player.video_user.load", objects -> runInsideUISync(() -> {
            model.setVideoUser((VideoUser) objects[0]);
            view.init();
        }));

        list.put("player.point.new", objects -> model.addPoint((Point) objects[0]));

        return list;
    }

    public void savePoint(Point newPoint) {
//        model.addPoint(newPoint);
        getApplication().getEventRouter().publishEventAsync("database.point.new", Arrays.asList(newPoint));
    }
}