package org.laeq;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("sub-menu")
public class SubMenuMVCGroup extends AbstractTypedMVCGroup<SubMenuModel, SubMenuView, SubMenuController> {
    public SubMenuMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}