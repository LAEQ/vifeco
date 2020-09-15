package org.laeq.db;

import java.util.Collection;

public interface DAOInterface<T> {
    public void insert(T data) throws DAOException;
    public Collection<T> findAll();
    public void delete(T data) throws DAOException;
}
