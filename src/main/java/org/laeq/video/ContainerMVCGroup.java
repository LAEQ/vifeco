package org.laeq.video;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("container")
public class ContainerMVCGroup extends AbstractTypedMVCGroup<ContainerModel, ContainerView, ContainerController> {
    public ContainerMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}