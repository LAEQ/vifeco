package user;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import org.laeq.user.UserListController;
import org.laeq.user.UserListModel;
import org.laeq.user.UserListView;

import javax.annotation.Nonnull;

@Named("user-list")
public class UserListMVCGroup extends AbstractTypedMVCGroup<UserListModel, UserListView, UserListController> {
    public UserListMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}