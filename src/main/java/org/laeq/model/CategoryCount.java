package org.laeq.model;

final public class CategoryCount {
    final public Category category;
    public Integer count;

    public CategoryCount(Category category, Integer count) {
        this.category = category;
        this.count = count;
    }

    public void increment(){
        this.count++;
    }

    public void decrement(){
        this.count--;
    }
}
