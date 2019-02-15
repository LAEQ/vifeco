package org.laeq.category;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.User;

import java.util.List;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class CategoryListModel extends AbstractGriffonModel {
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();

    public void add(Set<Category> list){
        categoryList.addAll(list);
    }
    public void add(Category video){
        categoryList.add(video);
    }
    public ObservableList<Category> getCategoryList() {
        return categoryList;
    }

    public void delete(Category category) {
        getCategoryList().remove(category);
    }
}