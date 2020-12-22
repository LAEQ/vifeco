package org.laeq.db

import org.hibernate.Session
import org.hibernate.Transaction
import org.laeq.model.Category
import org.laeq.model.Collection
import org.laeq.model.User
import org.laeq.model.Point
import spock.lang.Specification

import java.time.Duration


class HibernateEntityTest extends Specification {
    HibernateUtil hib
    Session session
    Transaction transaction

    def setup(){
        hib = new HibernateUtil('hibernate.cfg.xml')
        session = hib.sessionFactory.openSession()
        transaction = session.beginTransaction()
    }

    def cleanup(){
        session.close()
        hib.shutdown()
    }

    def "test create valid category"() {
        setup:
        Category category = new Category("mock name", "mock icon", "#FFFFFF", "A")
        session.save(category)
        transaction.commit()

        expect:
        category.getId() == 1
    }

    def "test create invalid category"(){
        setup:
        Category category = new Category("mock name", "mock icon", "#FFFFFF", "A")
        Category category2 = new Category("mock name", "mock icon", "#FFFFFF", "A")

        when:
        session.save(category)
        session.save(category2)
        transaction.commit();

        then:
        thrown Exception
    }

    def "test create user"(){
        setup:
        User user = new User("David", "Maignan", "davidmaignan@email.com")

        when:
        session.save(user)
        transaction.commit()

        then:
        user.getId() == 1
    }

    def "test invalid create user"(){
        setup:
        User user = new User()

        when:
        session.save(user)
        transaction.commit()

        then:
        thrown Exception
    }

    def "test create collection"(){
        setup:
        Collection collection = new Collection("mock collection")
        Category category = new Category("mock name", "mock icon", "#FFFFFF", "A")
        Category category2 = new Category("mock name 2", "mock icon", "#FFFFFF", "B")
        collection.addCategory(category)
        collection.addCategory(category2)

        when:
        session.save(collection)
        transaction.commit()

        then:
        category.getId() != category2.getId()

        collection.getId() == 1
    }

    def "test create video"(){
        setup:
        Collection collection = new Collection("mock collection")
        User user = new User("David", "Maignan", "davidmaignan@email.com")
        Point video = new Point("path", new Double(10), collection, user);


        when:
        session.save(collection)
        session.save(user)
        session.save(video)
        transaction.commit()

        then:
        video.getId() != null
    }

    def "test create point"(){
        setup:
        Collection collection = new Collection("mock collection")
        Category category = new Category("mock name", "mock icon", "#FFFFFF", "A")
        collection.addCategory(category)
        User user = new User("David", "Maignan", "davidmaignan@email.com")
        Point video = new Point("path", new Double(10), collection, user)
        Point point = new Point(1.0, 1.0, Duration.ofMillis(1000), category)

        when:
        session.save(collection)
        session.save(category)
        session.save(user)
        session.save(video)
        session.save(point)
        transaction.commit()

        then:
        collection.getId() != null
        category.getId() != null
        user.getId() != null
        point.getId() != null
    }
}
