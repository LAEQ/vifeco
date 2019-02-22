package org.laeq.category;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;

import javax.annotation.Nonnull;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class ContainerModel extends AbstractGriffonModel {
    //Table section
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();

    // Form Section
    private SimpleIntegerProperty id = new SimpleIntegerProperty(this, "id", 0);
    private SimpleStringProperty name = new SimpleStringProperty(this, "name", "");
    private SimpleStringProperty shortCut = new SimpleStringProperty(this, "shortCut", "");
    private SimpleStringProperty svgPath = new SimpleStringProperty(this, "svgPath", "");
    private SimpleStringProperty color = new SimpleStringProperty(this, "color", "#000000");
    private String errors = "";

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

    public String getSvgPath() {
        return svgPath.get();
    }
    public SimpleStringProperty svgPathProperty() {
        return svgPath;
    }
    public void setSvgPath(String svgPath) {
        this.svgPath.set(svgPath);
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
            builder.append(" - name");
        }

        if(getName().length() != 1 && getCategoryList().stream().filter(category -> category.getShortcut().equals(getShortCut())).findFirst().isPresent()){
            result = false;
            builder.append(" - shortcut");
        }

        if(! getColor().matches("^#[A-Z0-9]{6}$")){
            result = false;
            builder.append(" - Color");
        }

        return result;
    }

    public Category generateEntity() {
        Category category = new Category();
        category.setName(getName());
        category.setIcon(getSvgPath());
        category.setColor(getColor());
        category.setShortcut(getShortCut());

        return category;
    }

    public void addCategory(Category category) {
        categoryList.add(category);
    }
}