package org.laeq.menu;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.menu.BottomModel;
import org.laeq.menu.BottomView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonController.class)
public class BottomController extends AbstractGriffonController {
    @MVCMember @Nonnull private BottomModel model;
    @MVCMember @Nonnull private BottomView view;


}