package org.laeq.editor;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("display")
public class DisplayMVCGroup extends AbstractTypedMVCGroup<DisplayModel, DisplayView, DisplayController> {
    public DisplayMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}