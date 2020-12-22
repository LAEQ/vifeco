package org.laeq.model.dao;

import org.laeq.db.HibernateUtil;
import org.laeq.model.User;

import java.util.List;

public class UserDAO extends AbstractDAO<User> {
    public UserDAO(HibernateUtil hib) {
        super(hib);
    }

    @Override
    public void create(User user) {
        super.saveOrUpdate(user);
    }

    @Override
    public void delete(User user){
        super.delete(user);
    }

    @Override
    public List<User> findAll(){
        return super.findAll(User.class);
    }
}
