package org.laeq.model.dao


import org.laeq.model.User
import spock.lang.Specification

class UserDAOTest extends Specification {
    UserDAO dao
    HibernateUtil util
    void setup() {
        util = new HibernateUtil('hibernate.cfg.xml')
        dao = new UserDAO(util)
    }

    void cleanup() {
        util.shutdown()
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
        def result = dao.delete(user)

        then:
        result == false
    }

    def "FindDefault"() {
        setup:
        User user = EntityGenerator.createUser()
        user.setFirstName("I am default")
        user.setDefault(Boolean.TRUE)
        dao.create(user)

        for (i in 5) {
            dao.create(EntityGenerator.createUser())
        }

        when:
        User result = dao.findDefault()

        then:
        result.getFirstName() == "I am default"
    }
}
