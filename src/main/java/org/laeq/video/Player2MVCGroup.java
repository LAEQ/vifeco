package org.laeq.video;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import org.laeq.video.player.ContainerController;
import org.laeq.video.player.ContainerModel;
import org.laeq.video.player.ContainerView;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("player-2")
public class Player2MVCGroup extends AbstractTypedMVCGroup<ContainerModel, ContainerView, ContainerController> {
    public Player2MVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}