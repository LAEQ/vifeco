package org.laeq.model.dao

import org.laeq.db.HibernateUtil
import org.laeq.model.User
import spock.lang.Specification

class UserDAOTest extends Specification {
    UserDAO dao;

    void setup() {
        dao = new UserDAO(new HibernateUtil('hibernate.cfg.xml'))
    }

    void cleanup() {
    }

    def "test one insertion"(){
        setup:
        User user = EntityGenerator.createUser()

        when:
        dao.create(user)

        then:
        user.getId() != null
    }

    def "delete user"(){
        setup:
        User user = EntityGenerator.createUser()
        dao.create(user)

        when:
        dao.delete(user)

        then:
        notThrown Exception
    }

    def "delete default user fails"(){
        setup:
        User user = EntityGenerator.createUser()
        user.setDefault(Boolean.TRUE)
        dao.create(user)

        when:
        dao.delete(user)

        then:
        thrown Exception
    }
}
