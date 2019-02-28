package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties({"createdAt", "updatedAt" })
public class Category extends BaseEntity {
    private Integer id;
    private String name;
    private String icon;
    private String color;
    private String shortcut;

    public Category() {

    }

    public Category(Integer id, String name, String icon, String color, String shortcut) {
        this(name, icon, color, shortcut);
        this.id = id;
    }

    public Category(String name, String icon, String color, String shortcut) {
        this.name = name;
        this.icon = icon;
        this.shortcut = shortcut;
        this.color = color;
    }

    public int getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getShortcut() {
        return shortcut;
    }
    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
