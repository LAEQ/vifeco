package org.laeq.model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;


public abstract class AbstractDAO<T> {
    private HibernateUtil hib;
    protected Session session;
    protected Transaction transaction;

    public AbstractDAO(HibernateUtil hib){
        this.hib = hib;
    }

    protected Boolean saveOrUpdate(T obj) {
        Boolean result = true;
        try {

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<T>> violations = validator.validate(obj);

            if(violations.size() > 0){
                throw new Exception();
            }

            startOperation();
            session.saveOrUpdate(obj);
            transaction.commit();
        } catch (Exception e) {
            result = false;
            transaction.rollback();
        } finally {
            session.close();
        }

        return result;
    }

    protected Boolean delete(T obj)  {
        Boolean result = Boolean.TRUE;
        try {
            startOperation();
            session.delete(obj);
            transaction.commit();
        } catch (Exception e) {
            result = Boolean.FALSE;
        } finally {
            session.close();
        }

        return result;
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

    public abstract Boolean create(T object);
    public abstract List<T> findAll() throws Exception;
    public abstract T findOneById(int id) throws Exception;
}
