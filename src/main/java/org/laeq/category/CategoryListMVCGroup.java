package org.laeq.category;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("category-list")
public class CategoryListMVCGroup extends AbstractTypedMVCGroup<CategoryListModel, CategoryListView, CategoryListController> {
    public CategoryListMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}