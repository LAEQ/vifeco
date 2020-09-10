package org.laeq.db;

import java.util.Collection;
import java.util.Set;

public interface DAOInterface<T> {
    public void insert(T data) throws DAOException;
    public Collection<T> findAll();
    public void delete(T data) throws DAOException;
}
