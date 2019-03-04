package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@JsonIgnoreProperties({"proutProperty", "createdAt", "updatedAt"})
public class Collection extends BaseEntity {
    public final static String sequence_name = "category_collection_id";

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleBooleanProperty isDefault;
    private ObservableSet<Category> categorySet;

    private SimpleBooleanProperty proutProperty;

    public Collection() {
        this(0, "", false);
    }

    public Collection(int id, String name, boolean isDefault) {
        this.id = new SimpleIntegerProperty(this, "id", id);
        this.name = new SimpleStringProperty(this, "name", name);
        this.isDefault =  new SimpleBooleanProperty(isDefault);
        this.categorySet = FXCollections.observableSet();
        this.proutProperty = new SimpleBooleanProperty(true);
    }

    @JsonIgnore
    public boolean getProut() {
        return proutProperty.get();
    }

    public SimpleBooleanProperty proutProperty() {
        return proutProperty;
    }

    public void setProut(boolean proutProperty) {
        this.proutProperty.set(proutProperty);
    }

    public int getId() {
        return id.get();
    }
    public SimpleIntegerProperty idProperty() {
        return id;
    }
    public void setId(int id) {
        this.id.set(id);
    }
    public String getName() {
        return name.get();
    }
    public SimpleStringProperty nameProperty() {
        return name;
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public ObservableSet<Category> getCategorySet() {
        return categorySet;
    }
    public void setCategorySet(Set<Category> categorySet) {
        this.categorySet.addAll(categorySet);
    }
    public boolean isIsDefault() {
        return isDefault.get();
    }
    public SimpleBooleanProperty isDefaultProperty() {
        return isDefault;
    }
    public void setIsDefault(boolean isDefault) {
        this.isDefault.set(isDefault);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection that = (Collection) o;
        return id.getValue().equals(that.id.getValue()) &&
                name.getValue().equals(that.name.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.getValue(), name.getValue());
    }

    public void addCategory(Category category) {
        categorySet.add(category);
    }
    public void removeCategory(Category category){
        removeCategory(category.getId());
    }
    public void removeCategory(int id){
        categorySet.removeIf( category -> category.getId() == id);
    }

    @Override
    public String toString() {
        return getName();
    }

    @JsonIgnore
    public List<Integer> getCategoryIds() {
        return categorySet.stream().map(Category::getId).collect(toList());
    }

    @JsonIgnore
    public List<Category> getNewCategories(java.util.Collection ids){
        return categorySet.stream().filter(category -> ! ids.contains(category.getId())).collect(Collectors.toList());
    }
}
