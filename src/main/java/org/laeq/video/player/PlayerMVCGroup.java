package org.laeq.video.player;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import org.laeq.video.PlayerController;
import org.laeq.video.PlayerModel;
import org.laeq.video.PlayerView;

import javax.annotation.Nonnull;

@Named("player")
public class PlayerMVCGroup extends AbstractTypedMVCGroup<PlayerModel, PlayerView, PlayerController> {
    public PlayerMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}