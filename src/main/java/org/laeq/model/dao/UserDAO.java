package org.laeq.model.dao;

import org.laeq.db.HibernateUtil;
import org.laeq.model.User;

import java.util.List;

public class UserDAO extends AbstractDAO<User> {
    public UserDAO(HibernateUtil hib) {
        super(hib);
    }

    @Override
    public void create(User user) throws Exception {
        super.saveOrUpdate(user);
    }

    @Override
    public void delete(User user) throws Exception {
        if(user.getDefault() == Boolean.TRUE){
            throw new Exception("Cannot delete default user");
        }
        super.delete(user);
    }

    @Override
    public List<User> findAll(){
        return super.findAll(User.class);
    }
}
