package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "collection")
@JsonIgnoreProperties({ "id", "isDefault", "default"})
@JsonPropertyOrder({"name", "categories"})
public class Collection {
    @Id
    @GeneratedValue(generator = "increment")
    private Integer id;

    @Column(nullable = false)
    @Size(min = 1, max = 255)
    private String name;

    @Column()
    @ColumnDefault("False")
    private Boolean isDefault;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER, targetEntity = Category.class)
    @JoinTable(
            name = "collection_category",
            joinColumns = { @JoinColumn(name = "collection_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") }
    )
    private Set<Category> categories = new HashSet<>();

    public Collection() {}

    public Collection(String name) {
        this();
        this.name = name;
    }

    public Collection(Integer id, @Size(min = 1, max = 255) String name, Boolean isDefault) {
        this.id = id;
        this.name = name;
        this.isDefault = isDefault;
        this.categories = new HashSet<>();
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

    public Boolean getDefault() {
        return isDefault;
    }
    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Set<Category> getCategories() {
        return categories;
    }
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @JsonIgnore
    public void addCategory(Category category) {
        categories.add(category);
    }
    public void removeCategory(Category category){
        removeCategory(category.getId());
    }
    public void removeCategory(int id){
        categories.removeIf(category -> category.getId() == id);
    }

    @JsonIgnore
    public boolean hasCategory(Category category){
        return categories.contains(category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection that = (Collection) o;
        return Objects.equals(id, that.id) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return name;
    }

    @JsonIgnore
    public String getCategorieNames() {
        return categories.stream().map(e -> e.getName()).collect(Collectors.joining("\n"));
    }
}
