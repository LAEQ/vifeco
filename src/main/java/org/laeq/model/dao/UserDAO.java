package org.laeq.model.dao;

import org.laeq.model.User;

import javax.persistence.Query;
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
    public List<User> findAll() throws Exception {
        return super.findAll(User.class);
    }

    @Override
    public User findOneById(int id) throws Exception {
        return super.findById(id, User.class);
    }

    public User findDefault() throws Exception {
        User result = null;
        try{
            startOperation();
            Query query= session.createQuery("from User where isDefault = true");
            result = (User) query.getSingleResult();
        } finally {
            session.close();
        }

        return result;
    }
}
