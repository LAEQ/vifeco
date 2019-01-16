package org.laeq;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import javax.annotation.Nonnull;
import java.util.Arrays;

@ArtifactProviderFor(GriffonController.class)
public class BottomController extends AbstractGriffonController {
    private BottomModel model;

    @MVCMember
    public void setModel(@Nonnull BottomModel model) {
        this.model = model;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void bottomTest() {
        System.out.println("bottomTest");

        String a[] = new String[]{"abc","klm","xyz","pqr"};

        getApplication().getEventRouter().publishEventAsync("Ping", Arrays.asList(a));
    }
}