package org.laeq.model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.laeq.model.Point;
import org.laeq.model.Video;

import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PointDAO extends AbstractDAO<Point> {
    public PointDAO(HibernateUtil hib) {
        super(hib);
    }

    @Override
    public Boolean create(Point point)  {
        return super.saveOrUpdate(point);
    }

    @Override
    public Boolean delete(Point point)  {
        Boolean result = true;
        Session currentSession = this.hib.sessionFactory.getCurrentSession();
        Transaction tx = currentSession.beginTransaction();
        try {
            currentSession.delete(point);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            tx.rollback();
        } finally {
            currentSession.close();
        }

        return result;
    }

    @Override
    public List<Point> findAll() throws Exception {
        return super.findAll(Point.class);
    }

    @Override
    public Point findOneById(int id) throws Exception {
        return super.findById(id, Point.class);
    }
}
