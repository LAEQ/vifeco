package org.laeq.template;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("middle-pane")
public class MiddlePaneMVCGroup extends AbstractTypedMVCGroup<MiddlePaneModel, MiddlePaneView, MiddlePaneController> {
    public MiddlePaneMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}