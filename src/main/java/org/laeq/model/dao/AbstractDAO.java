package org.laeq.model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.laeq.db.HibernateUtil;
import org.laeq.model.Collection;

import java.util.List;


public abstract class AbstractDAO<T> {
    private HibernateUtil hib;
    private Session session;
    private Transaction transaction;

    public AbstractDAO(HibernateUtil hib){
        this.hib = hib;
    }

    protected void saveOrUpdate(T obj) throws Exception{
        try {
            startOperation();
            session.saveOrUpdate(obj);
            transaction.commit();
        } finally {
            session.close();
        }
    }

    protected void delete(T obj) throws Exception {
        try {
            startOperation();
            session.delete(obj);
            transaction.commit();
        } finally {
            session.close();
        }
    }

    protected List findAll(Class clazz) throws Exception {
        List objects = null;
        try {
            startOperation();
            Query query = session.createQuery("from " + clazz.getName());
            objects = query.list();
            transaction.commit();
        } finally {
            session.close();
        }
        return objects;
    }

    protected T findById(int id, Class clazz) throws Exception{
        Object result = null;
        try{
            startOperation();
            result = session.get(clazz, id);
        } finally {
            session.close();
        }

        return (T)result;
    }

    protected void startOperation() throws Exception {
        session = hib.sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    public abstract void create(T object) throws Exception;
    public abstract List<T> findAll() throws Exception;
    public abstract T findOneById(int id) throws Exception;
}
