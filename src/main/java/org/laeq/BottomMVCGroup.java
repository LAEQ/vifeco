package org.laeq;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("bottom")
public class BottomMVCGroup extends AbstractTypedMVCGroup<BottomModel, BottomView, BottomController> {
    public BottomMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}