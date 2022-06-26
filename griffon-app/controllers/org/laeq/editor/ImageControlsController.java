package org.laeq.editor;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import java.util.Arrays;

@ArtifactProviderFor(GriffonController.class)
public class ImageControlsController extends AbstractGriffonController {
    @MVCMember @Nonnull private ImageControlsModel model;
    @MVCMember @Nonnull private ImageControlsView view;
    @MVCMember @Nonnull private ImageControls controls;

    public void dispatch(String eventName, Double value) {

        if(value != null){
            getApplication().getEventRouter().publishEventOutsideUI(eventName, Arrays.asList(value));
        }
    }
}