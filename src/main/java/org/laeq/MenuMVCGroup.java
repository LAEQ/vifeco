package org.laeq;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("menu")
public class MenuMVCGroup extends AbstractTypedMVCGroup<MenuModel, MenuView, MenuController> {
    public MenuMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}