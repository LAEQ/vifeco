package org.laeq.user;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("user")
public class UserMVCGroup extends AbstractTypedMVCGroup<UserModel, UserView, UserController> {
    public UserMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}