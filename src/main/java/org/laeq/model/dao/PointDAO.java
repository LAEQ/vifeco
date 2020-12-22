package org.laeq.model.dao;

import org.laeq.db.HibernateUtil;
import org.laeq.model.Point;

import java.util.List;

public class PointDAO extends AbstractDAO<Point> {
    public PointDAO(HibernateUtil hib) {
        super(hib);
    }

    @Override
    public void create(Point point) throws Exception {
        super.saveOrUpdate(point);
    }

    @Override
    public void delete(Point point) throws Exception {
        super.delete(point);
    }

    @Override
    public List<Point> findAll(){
        return super.findAll(Point.class);
    }
}
