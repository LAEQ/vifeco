package org.laeq.user;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import org.laeq.UserController;
import org.laeq.UserModel;
import org.laeq.UserView;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("container")
public class ContainerMVCGroup extends AbstractTypedMVCGroup<UserModel, UserView, UserController> {
    public ContainerMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}