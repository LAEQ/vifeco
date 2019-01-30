package org.laeq.db;

import org.laeq.model.User;

import java.util.Set;

public interface DAOInterface<T> {
    public boolean insert(T data);
    public Set<T> findAll();

    public boolean delete(T data);
}
