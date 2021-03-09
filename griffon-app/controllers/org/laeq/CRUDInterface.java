package org.laeq;

public interface CRUDInterface<T> {
    void save();
    void delete(T object);
    void clear();
}
