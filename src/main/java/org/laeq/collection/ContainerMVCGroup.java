package org.laeq.collection;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import org.laeq.CollectionController;
import org.laeq.CollectionModel;
import org.laeq.CollectionView;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("container")
public class ContainerMVCGroup extends AbstractTypedMVCGroup<CollectionModel, CollectionView, CollectionController> {
    public ContainerMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}