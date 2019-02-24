package org.laeq;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import org.laeq.menu.BottomController;
import org.laeq.menu.BottomModel;
import org.laeq.menu.BottomView;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("bottom")
public class BottomMVCGroup extends AbstractTypedMVCGroup<BottomModel, BottomView, BottomController> {
    public BottomMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}