package org.laeq.editor;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ControlsController extends AbstractGriffonController {
    @MVCMember @Nonnull private ControlsModel model;
    @MVCMember @Nonnull private ControlsView view;
    @MVCMember @Nonnull private Controls controls;


    @Override
    public void mvcGroupDestroy(){
        Stage controls = (Stage) getApplication().getWindowManager().findWindow("controls");
        if(controls != null){
            getApplication().getWindowManager().detach("controls");
            controls.close();
        }

        System.out.println("controls: " + getApplication().getMvcGroupManager().getGroups().keySet());
        System.out.println("controls: " + getApplication().getWindowManager().getWindowNames());
    }

    public void dispatch(String eventName, double value) {
        getApplication().getEventRouter().publishEvent(eventName, Arrays.asList(value));
    }
}