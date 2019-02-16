package org.laeq.category;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.CategoryCollection;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;

@ArtifactProviderFor(GriffonModel.class)
public class CategoryCollectionModel extends AbstractGriffonModel {
    private SimpleStringProperty name = new SimpleStringProperty("");
    private ObservableMap<SimpleBooleanProperty, Category> categorySet = FXCollections.observableHashMap();
    private ObservableSet<Category> categories = FXCollections.observableSet();

    public ObservableMap<SimpleBooleanProperty, Category> getCategorySet() {
        return categorySet;
    }

    public SimpleBooleanProperty addCategory(Category category){
        SimpleBooleanProperty sbp = new SimpleBooleanProperty(false);
        categorySet.put(sbp, category);

        return sbp;
    }

    public ObservableSet<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories.addAll(categories);
    }

    @Nonnull
    public final StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        nameProperty().set(name);
    }

    public String getName() {
        return nameProperty().get();
    }

    public CategoryCollection generateEntity() {
        CategoryCollection categoryCollection = new CategoryCollection();

        categoryCollection.setName(getName());

        Set<Category> selectedCategories = categorySet.entrySet().stream().filter(s -> s.getKey().getValue()).map(map -> map.getValue()).collect(Collectors.toSet());

        categoryCollection.getCategorySet().addAll(selectedCategories);

        return categoryCollection;
    }

    public void clear() {
        setName("");
        categorySet.keySet().forEach(sbp -> sbp.setValue(false));
    }
}