package org.laeq.category;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import org.laeq.CategoryController;
import org.laeq.CategoryModel;
import org.laeq.CategoryView;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("container")
public class ContainerMVCGroup extends AbstractTypedMVCGroup<CategoryModel, CategoryView, CategoryController> {
    public ContainerMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}