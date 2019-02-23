package org.laeq.video;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.event.Event;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import java.util.Arrays;

@ArtifactProviderFor(GriffonController.class)
public class ControlsController extends AbstractGriffonController {
    @MVCMember @Nonnull private ControlsModel model;
    @MVCMember @Nonnull private ControlsView view;

    @MVCMember
    public void setModel(@Nonnull ControlsModel model) {
        this.model = model;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void sizePointActionTarget(Event event){
        System.out.println(event);
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void volumeChangeEvent() {
//        Integer volume[] = new Integer[]{model.getVolume()};
        getApplication().getEventRouter().publishEventAsync("controls.volume.change");
    }

    public void changeRate(Number newValue) {
        dispatchEvent("controls.rate", newValue);
    }

    public void changeVolume(Number newValue) {
        dispatchEvent("controls.volume", newValue);
    }

    public void changeSize(Number newValue) {
        dispatchEvent("controls.size", newValue);
    }

    public void changeDuration(Number newValue) {
        dispatchEvent("controls.duration", newValue);
    }

    public void changeOpacity(Number oldvalue, Number newValue) {
        dispatchEvent("controls.opacity", oldvalue, newValue);
    }

    private void dispatchEvent(String eventName, Number value){
        getApplication().getEventRouter().publishEventAsync(eventName, Arrays.asList(value));
    }

    private void dispatchEvent(String eventName, Number oldvalue, Number newvalue){
        getApplication().getEventRouter().publishEventAsync(eventName, Arrays.asList(oldvalue, newvalue));
    }
}