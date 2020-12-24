package org.laeq.model;

import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;


@Entity
@Table(name = "collection")
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
    private Set<Category> categories;

    public Collection() {
        categories = new HashSet<>();
    }

    public Collection(String name) {
        this();
        this.name = name;
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

    public void addCategory(Category category) {
        categories.add(category);
    }
    public void removeCategory(Category category){
        removeCategory(category.getId());
    }
    public void removeCategory(int id){
        categories.removeIf(category -> category.getId() == id);
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
//    @JsonIgnore
//    public List<Integer> getCategoryIds() {
//        return categorySet.stream().map(Category::getId).collect(toList());
//    }
//
//    @JsonIgnore
//    public List<Category> getNewCategories(java.util.Collection ids){
//        return categorySet.stream().filter(category -> ! ids.contains(category.getId())).collect(Collectors.toList());
//    }
}
