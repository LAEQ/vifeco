package org.laeq.model.dao;

import org.laeq.db.HibernateUtil;
import org.laeq.model.Category;
import org.laeq.model.Collection;

import java.util.List;

public class CollectionDAO extends AbstractDAO<Collection> {
    public CollectionDAO(HibernateUtil hib) {
        super(hib);
    }

    @Override
    public void create(Collection collection) throws Exception {
        super.saveOrUpdate(collection);
    }

    @Override
    public void delete(Collection collection) throws Exception {
        if(collection.getDefault() == Boolean.TRUE){
            throw new Exception("");
        }
        super.delete(collection);
    }

    @Override
    public List<Collection> findAll(){
        return super.findAll(Collection.class);
    }
}
