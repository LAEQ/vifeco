package org.laeq.video;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.event.Event;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonController.class)
public class ControlsController extends AbstractGriffonController {
    private ControlsModel model;

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
}