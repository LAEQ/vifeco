package org.laeq.editor;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("video_filter")
public class FilterMVCGroup extends AbstractTypedMVCGroup<ControlsModel, ControlsView, ControlsController> {
    public FilterMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}