package org.laeq.db;

import org.laeq.model.User;

import java.util.Set;

public interface DAOInterface<T> {
    public void insert(T data) throws DAOException;
    public Set<T> findAll();
    public void delete(T data) throws DAOException;
}
