package org.laeq;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("main")
public class MainMVCGroup extends AbstractTypedMVCGroup<MainModel, MainView, MainController> {
    public MainMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}