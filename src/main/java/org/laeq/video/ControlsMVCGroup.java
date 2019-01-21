package org.laeq.video;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("controls")
public class ControlsMVCGroup extends AbstractTypedMVCGroup<ControlsModel, ControlsView, ControlsController> {
    public ControlsMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}