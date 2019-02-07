package org.laeq.model;

import java.util.Objects;

public class Category extends Entity{
    private Integer id;
    private String name;
    private String icon;
    private String shortcut;

    public Category() {

    }

    public Category(Integer id, String name, String icon, String shortcut) {
        this(name, icon, shortcut);
        this.id = id;
    }

    public Category(String name, String icon, String shortcut) {
        this.name = name;
        this.icon = icon;
        this.shortcut = shortcut;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
