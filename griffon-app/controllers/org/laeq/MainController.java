package org.laeq;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonController.class)
public class MainController extends AbstractGriffonController {
    @MVCMember
    private MainModel model;

    @MVCMember
    private MainView view;

    @MVCMember
    public void setModel(@Nonnull MainModel model) {
        this.model = model;
    }

    @MVCMember void setView(@Nonnull MainView view) {
        this.view = view;
    }

//    @ControllerAction
//    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
//    public void click() {
//        int count = Integer.parseInt(model.getClickCount());
//        model.setClickCount(String.valueOf(count + 1));
//    }
}