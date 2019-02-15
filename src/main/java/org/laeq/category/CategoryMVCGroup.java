package org.laeq.category;

import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;

import javax.annotation.Nonnull;
import javax.inject.Named;

@Named("category")
public class CategoryMVCGroup extends AbstractTypedMVCGroup<CategoryModel, CategoryView, CategoryController> {
    public CategoryMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}