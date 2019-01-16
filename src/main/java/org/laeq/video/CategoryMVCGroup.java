package org.laeq.video;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("category")
public class CategoryMVCGroup extends AbstractTypedMVCGroup<CategoryModel, CategoryView, CategoryController> {
    public CategoryMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}