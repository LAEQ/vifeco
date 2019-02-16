package org.laeq.category;

import javax.inject.Named;
import griffon.core.mvc.MVCGroup;
import org.codehaus.griffon.runtime.core.mvc.AbstractTypedMVCGroup;
import javax.annotation.Nonnull;

@Named("category-collection")
public class CategoryCollectionMVCGroup extends AbstractTypedMVCGroup<CategoryCollectionModel, CategoryCollectionView, CategoryCollectionController> {
    public CategoryCollectionMVCGroup(@Nonnull MVCGroup delegate) {
        super(delegate);
    }
}