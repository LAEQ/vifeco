package org.laeq;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonController.class)
public class VifecoController extends AbstractGriffonController {
    private VifecoModel model;

    @MVCMember
    public void setModel(@Nonnull VifecoModel model) {
        this.model = model;
    }
}