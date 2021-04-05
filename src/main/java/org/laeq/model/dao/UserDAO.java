package org.laeq.model.dao;

import org.laeq.model.User;

import javax.persistence.Query;
import java.util.List;

public class UserDAO extends AbstractDAO<User> {
    public UserDAO(HibernateUtil hib) {
        super(hib);
    }

    @Override
    public Boolean create(User user) {
        return super.saveOrUpdate(user);
    }

    @Override
    public Boolean delete(User user) {
        return super.delete(user);
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
