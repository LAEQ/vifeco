package org.laeq.video;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("controls")
public class ControlsMVCGroup extends AbstractTypedMVCGroup<ControlsModel, ControlsView, ControlsController> {
    public ControlsMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}