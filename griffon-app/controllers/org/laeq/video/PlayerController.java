package org.laeq.video;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import javax.annotation.Nonnull;
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

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener("menu.open.video", files -> {
            File file = (File) files[0];

            System.out.println(file.toString());
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
}