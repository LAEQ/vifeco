package org.laeq.video;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("player-2")
public class Player2MVCGroup extends AbstractTypedMVCGroup<Player2Model, Player2View, Player2Controller> {
    public Player2MVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}