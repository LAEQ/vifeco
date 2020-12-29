package org.laeq.model.dao;

import org.laeq.model.Collection;

import javax.persistence.Query;
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
    public List<Collection> findAll() throws Exception {
        return super.findAll(Collection.class);
    }

    @Override
    public Collection findOneById(int id) throws Exception{
        return super.findById(id, Collection.class);
    }


    public Collection findDefault() throws Exception {
        Collection result = null;
        try{
            startOperation();
            Query query= session.createQuery("from Collection where isDefault = true");
            result = (Collection) query.getSingleResult();
        } finally {
            session.close();
        }

        return result;
    }
}
