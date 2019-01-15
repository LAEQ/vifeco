package org.laeq;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonController.class)
public class MenuController extends AbstractGriffonController {
    private MenuModel model;

    @MVCMember
    public void setModel(@Nonnull MenuModel model) {
        this.model = model;
    }

    public void open() {
        System.out.println("Click");
    }
}