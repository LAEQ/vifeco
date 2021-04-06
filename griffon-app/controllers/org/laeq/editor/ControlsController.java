package org.laeq.editor;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import java.util.Arrays;

@ArtifactProviderFor(GriffonController.class)
public class ControlsController extends AbstractGriffonController {
    @MVCMember @Nonnull private ControlsModel model;
    @MVCMember @Nonnull private ControlsView view;
    @MVCMember @Nonnull private Controls controls;


    @Override
    public void mvcGroupDestroy(){

    }

    public void dispatch(String eventName, double value) {
        getApplication().getEventRouter().publishEventOutsideUI(eventName, Arrays.asList(value));
    }
}