package org.laeq.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class CategoryCollection extends BaseEntity {
    public final static String sequence_name = "category_collection_id";

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleBooleanProperty isDefault;
    private Set<Category> categorySet;

    public CategoryCollection() {
        this(0, "", false);
    }

    public CategoryCollection(int id, String name, boolean isDefault) {
        this.id = new SimpleIntegerProperty(this, "id", id);
        this.name = new SimpleStringProperty(this, "name", name);
        this.isDefault =  new SimpleBooleanProperty(isDefault);
        this.categorySet = new HashSet<>();
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
    public Set<Category> getCategorySet() {
        return categorySet;
    }
    public void setCategorySet(Set<Category> categorySet) {
        this.categorySet = categorySet;
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
        CategoryCollection that = (CategoryCollection) o;
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

    public List<Integer> getCategoryIds() {
        return categorySet.stream().map(Category::getId).collect(toList());
    }

    public List<Category> getNewCategories(Collection<Integer> ids){
        return categorySet.stream().filter(category -> ! ids.contains(category.getId())).collect(Collectors.toList());
    }
}
