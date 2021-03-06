package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.validator.constraints.Length;
import org.laeq.model.icon.IconPoint;
import org.laeq.model.icon.IconSize;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity()
@Table(name="category")
@JsonIgnoreProperties({"icon", "color", "shortcut", "collections"})
@JsonPropertyOrder({"id", "name"})
public class Category implements Comparable<Category> {
    @Id
    @GeneratedValue(generator = "increment")
    private Integer id;

    @Column(nullable = false)
    @Size(min = 1, max = 255)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(min = 1)
    private String icon;

    @Column(nullable = false)
    @Length(min = 7, max = 7)
    @Pattern(regexp = "^#[0-9A-F]{6}$")
    private String color;

    @Column(nullable = false, unique = true)
    @Length(min = 1, max = 1)
    private String shortcut;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY, mappedBy = "categories")
    private Set<Collection> collections = new HashSet<>();


    public Category() {
    }

    public Category(Integer id) {
        this.id = id;
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

    public Category(String[] values) {
        super();
        this.setName(values[0]);
        this.setIcon(values[1]);
        this.setColor(values[2]);
        this.setShortcut(values[3]);
    }

    public Integer getId() {
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

    @JsonIgnore
    public Icon getIcon2() {
        return new Icon(this.icon, this.color);
    }

    @JsonIgnore
    public IconPoint getIconPoint() {
        return new IconPoint(new IconSize(this, 20));
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public Set<Collection> getCollections() {
        return collections;
    }

    public void setCollections(Set<Collection> collections) {
        this.collections = collections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id;
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

    @Override
    public int compareTo(Category o) {
        return this.name.toLowerCase().compareTo(o.name.toLowerCase());
    }
}
