package org.laeq.video;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class PlayerController extends AbstractGriffonController {
    private PlayerModel model;

    @MVCMember
    public void setModel(@Nonnull PlayerModel model) {
        this.model = model;
    }

    @MVCMember @Nonnull
    private PlayerView view;

    @Inject private DialogService dialogService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener("controls.volume.change", volume -> {
            runInsideUIAsync(()->{
                view.setVolume();
            });
        });

        getApplication().getEventRouter().addEventListener("menu.open.video", files -> {
            //@todo: check file exists
            File file = (File) files[0];

            runInsideUISync(() -> {
                model.setVideoPath(file.toString());
                view.setMedia(file.toString());
                model.setIsPlaying(false);
            });
        });
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
}