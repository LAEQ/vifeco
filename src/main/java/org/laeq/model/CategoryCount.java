package org.laeq.model;

import javafx.beans.property.SimpleIntegerProperty;

final public class CategoryCount {
    final public Category category;
    public Integer count;
    public SimpleIntegerProperty total = new SimpleIntegerProperty();

    public CategoryCount(Category category, Integer count) {
        this.category = category;
        this.count = count;
        total.set(count);
    }

    public void increment(){
        this.count++;
        total.set(count);
    }

    public void decrement(){
        this.count--;
        total.set(count);
    }
}
