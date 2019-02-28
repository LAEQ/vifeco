package org.laeq.system;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("status")
public class StatusMVCGroup extends AbstractTypedMVCGroup<StatusModel, StatusView, StatusController> {
    public StatusMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}