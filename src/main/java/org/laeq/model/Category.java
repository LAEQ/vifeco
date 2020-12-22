package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;


import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Entity()
@Table(name="category")
@JsonIgnoreProperties({"createdAt", "updatedAt" })
public class Category extends BaseEntity {
    @Id @GeneratedValue(generator = "increment")
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String icon;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false, unique = true)
    private String shortcut;

    @ManyToMany(mappedBy = "collections")
    private List<Collection> collectionList;

    public Category() {
    }

    public Category(int id){
        this.id = id;
    }

    public Category(int id, String name, String icon, String color, String shortcut) {
        this(name, icon, color, shortcut);
        this.id = id;
    }

    public Category(String name, String icon, String color, String shortcut) {
        this.name = name;
        this.icon = icon;
        this.shortcut = shortcut;
        this.color = color;
    }

    public Category(String[] values) {
        super();
        this.setName(values[0]);
        this.setIcon(values[1]);
        this.setColor(values[2]);
        this.setShortcut(values[3]);
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
        return id == category.id && name.equals(category.name);
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
