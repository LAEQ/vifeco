package org.laeq.model.dao;

import org.laeq.model.Drawing;
import java.util.List;

public class DrawingDAO extends AbstractDAO<Drawing> {
    public DrawingDAO(HibernateUtil hib) {
        super(hib);
    }

    @Override
    public Boolean create(Drawing drawing) {
        return super.saveOrUpdate(drawing);
    }

    @Override
    public Boolean delete(Drawing drawing) {
        return super.delete(drawing);
    }

    @Override
    public List<Drawing> findAll() throws Exception {
        return super.findAll(Drawing.class);
    }

    @Override
    public Drawing findOneById(int id) throws Exception {
        return super.findById(id, Drawing.class);
    }
}
