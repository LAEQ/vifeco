package org.laeq.model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.laeq.db.HibernateUtil;
import java.util.List;


public abstract class AbstractDAO<T> {
    private HibernateUtil hib;
    private Session session;
    private Transaction transaction;

    public AbstractDAO(HibernateUtil hib){
        this.hib = hib;
    }

    protected void saveOrUpdate(T obj) {
        try {
            startOperation();
            session.saveOrUpdate(obj);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public abstract void create(T category);

    protected void delete(T obj) {
        try {
            startOperation();
            session.delete(obj);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    protected List findAll(Class clazz) {
        List objects = null;
        try {
            startOperation();
            Query query = session.createQuery("from " + clazz.getName());
            objects = query.list();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return objects;
    }

    protected void startOperation() throws Exception {
        session = hib.sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    public abstract List<T> findAll();
}
