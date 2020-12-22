package org.laeq.model.dao;

import org.laeq.db.HibernateUtil;
import org.laeq.model.Category;

import java.util.List;

public class CategoryDAO extends AbstractDAO<Category> {
    public CategoryDAO(HibernateUtil hib) {
        super(hib);
    }

    @Override
    public void create(Category category){
        super.saveOrUpdate(category);
    }

    @Override
    public void delete(Category category){
        super.delete(category);
    }

    @Override
    public List<Category> findAll(){
        return super.findAll(Category.class);
    }
}
