package org.laeq.editor;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("player")
public class PlayerMVCGroup extends AbstractTypedMVCGroup<PlayerModel, PlayerView, PlayerController> {
    public PlayerMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}