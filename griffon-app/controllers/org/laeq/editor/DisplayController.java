package org.laeq.editor;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class DisplayController extends AbstractGriffonController {
    @MVCMember @Nonnull private DisplayModel model;
    @MVCMember @Nonnull private DisplayView view;

    @MVCMember @Nonnull private File file;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }

    @Override
    public void mvcGroupDestroy(){
        Stage display = (Stage) getApplication().getWindowManager().findWindow("display");
        if(display != null){
            display.close();
        }

        System.out.println("Display video controller destroyed.");
        System.out.println("B: " + getApplication().getMvcGroupManager().getGroups().keySet());
        System.out.println("B: " + getApplication().getWindowManager().getWindowNames());
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void volume() {
        if(model.volume.getValue()){
            model.volume.set(Boolean.FALSE);
            view.volumeOff();
        } else {
            model.volume.set(Boolean.TRUE);
            view.volumeOn();
        }
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("player.pause", objects -> {
            runInsideUISync(() -> {
                view.pause();
            });
        });

        list.put("player.play", objects -> {
            runInsideUISync(() -> {
                view.play();
            });
        });

        list.put("player.currentTime", objects -> {
            runOutsideUI(() -> {
                Duration currentTime = (Duration) objects[0];
                view.seek(currentTime);
            });
        });

        return list;
    }

    public void isReady() {
        getApplication().getEventRouter().publishEvent("display.ready");
    }
}