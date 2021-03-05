package org.laeq;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;

@ArtifactProviderFor(GriffonModel.class)
public class CategoryModel extends AbstractGriffonModel {
    //Table section
    public ObservableList<Category> categoryList = FXCollections.observableArrayList();

    // Form Section
    public Category selectedCategory;
    public SimpleIntegerProperty id = new SimpleIntegerProperty(this, "id", 0);
    public SimpleStringProperty name = new SimpleStringProperty(this, "name", "");
    public SimpleStringProperty shortCut = new SimpleStringProperty(this, "shortCut", "");
    public SimpleStringProperty icon = new SimpleStringProperty(this, "icon", "");
    public SimpleStringProperty color = new SimpleStringProperty(this, "color", "");

    public void addCategory(Category category) {
        categoryList.add(category);
    }

    public void setSelectedCategory(Category category) {
        this.selectedCategory = category;
        name.set(category.getName());
        icon.set(category.getIcon());
        color.set(category.getColor());
        shortCut.set(category.getShortcut());
    }

    public Category getCategory() {
        if(this.selectedCategory == null){
           this.selectedCategory = new Category();
        }

        this.selectedCategory.setName(name.get());
        this.selectedCategory.setIcon(icon.get());
        this.selectedCategory.setColor(color.get());
        this.selectedCategory.setShortcut(shortCut.get());

        return this.selectedCategory;
    }

    public void clear() {
        name.set("");
        icon.set("");
        color.set("");
        shortCut.set("");

        this.selectedCategory = null;
    }
}