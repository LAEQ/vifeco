package org.laeq.category;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.Preferences;

import java.util.Optional;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class ContainerModel extends AbstractGriffonModel {
    //Table section
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();

    // Form Section
    private Category selectedCategory;
    private SimpleIntegerProperty id = new SimpleIntegerProperty(this, "id", 0);
    private SimpleStringProperty name = new SimpleStringProperty(this, "name", "");
    private SimpleStringProperty shortCut = new SimpleStringProperty(this, "shortCut", "");
    private SimpleStringProperty icon = new SimpleStringProperty(this, "icon", "");
    private SimpleStringProperty color = new SimpleStringProperty(this, "color", "#00000000");
    private String errors = "";
    public Preferences prefs;

    public ObservableList<Category> getCategoryList() {
        return categoryList;
    }
    public void addCategories(Set<Category> categorySet) {
        this.categoryList.addAll(categorySet);
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

    public String getShortCut() {
        return shortCut.get();
    }
    public SimpleStringProperty shortCutProperty() {
        return shortCut;
    }
    public void setShortCut(String shortCut) {
        this.shortCut.set(shortCut);
    }

    public String getIcon() {
        return icon.get();
    }
    public SimpleStringProperty iconProperty() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon.set(icon);
    }

    public String getColor() {
        return color.get();
    }
    public SimpleStringProperty colorProperty() {
        return color;
    }
    public void setColor(String color) {
        this.color.set(color);
    }

    public String getErrors() {
        return errors;
    }
    public void setErrors(String errors) {
        this.errors = errors;
    }

    public Boolean valid() {
        Boolean result = true;

        StringBuilder builder = new StringBuilder("Some fields are invalid: \n");

        if(getName().length() == 0){
            result = false;
            builder.append(" - name\n");
        }

        if(getName().length() != 1 && validShortCut()){
            result = false;
            builder.append(" - shortcut\n");
        }

        if(getIcon().length() == 0){
            result = false;
            builder.append(" - icon\n");
        }

        if(! getColor().matches("^#[A-Za-z0-9]{6}$")){
            result = false;
            builder.append(" - Color\n");
        }

        errors = builder.toString();

        return result;
    }

    private Boolean validShortCut(){
        Optional<Category> optionalCategory = getCategoryList().stream().filter(category -> category.getShortcut().equals(getShortCut())).findFirst();

        if(this.selectedCategory == null){
            return optionalCategory.isPresent();
        }

        return optionalCategory.isPresent() && (optionalCategory.get().getId() != this.selectedCategory.getId());
    }

    public Category generateEntity() {
        Category category = new Category();
        category.setName(getName());
        category.setIcon(getIcon());
        category.setColor(getColor());
        category.setShortcut(getShortCut());

        if(this.selectedCategory != null){
            category.setId(this.selectedCategory.getId());
        }

        return category;
    }

    public void addCategory(Category category) {
        categoryList.add(category);
    }

    public void setSelectedCategory(Category category) {
        this.selectedCategory = category;

        setId(category.getId());
        setName(category.getName());
        setIcon(category.getIcon());
        setShortCut(category.getShortcut());
        setColor(category.getColor());
    }

    public void updateCategory(Category category) {
        if(this.selectedCategory !=null){
            this.selectedCategory.setName(category.getName());
            this.selectedCategory.setIcon(category.getIcon());
            this.selectedCategory.setColor(category.getColor());
            this.selectedCategory.setShortcut(category.getShortcut());
        }

        clear();
    }

    public void delete(Category category) {
        categoryList.remove(category);
    }

    public void clear() {
        setName("");
        setShortCut("");
        setColor("");
        setIcon("");
        setId(0);
        this.selectedCategory = null;
    }

    public Preferences getPrefs() {
        return prefs;
    }

    public void setPrefs(Preferences prefs) {
        this.prefs = prefs;
    }
}