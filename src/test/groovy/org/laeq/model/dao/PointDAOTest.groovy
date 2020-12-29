package org.laeq.model.dao

import javafx.util.Duration
import org.laeq.model.*
import spock.lang.Specification

class PointDAOTest extends Specification {
    VideoDAO videoDAO;
    CollectionDAO colDAO
    CategoryDAO catDAO
    UserDAO userDAO
    PointDAO pointDAO
    Category category_1, category_2
    Collection collection
    User user
    Video video


    void setup() {
        videoDAO = new VideoDAO(new HibernateUtil('hibernate.cfg.xml'))
        catDAO = new CategoryDAO(new HibernateUtil('hibernate.cfg.xml'))
        colDAO = new CollectionDAO(new HibernateUtil('hibernate.cfg.xml'))
        userDAO = new UserDAO(new HibernateUtil('hibernate.cfg.xml'))
        pointDAO = new PointDAO(new HibernateUtil('hibernate.cfg.xml'))

        user = EntityGenerator.createUser()
        userDAO.create(user)

        category_1 = EntityGenerator.createCategory("A")
        category_2 = EntityGenerator.createCategory("B")

        collection = new Collection("mock col 1")
        collection.addCategory(category_1)
        collection.addCategory(category_2)

        catDAO.create(category_1)
        catDAO.create(category_2)
        colDAO.create(collection)

        video = new Video('mock/path', Duration.millis(1000), collection, user)
        videoDAO.create(video)
    }

    void cleanup() {

    }

    def "test one insertion"(){
        setup:
        Point point = new Point(1,1,Duration.ONE, category_1, video)

        when:
        pointDAO.create(point)

        then:
        point.getId() != null
        pointDAO.findAll().size() == 1
    }

    def "delete one point"(){
        setup:
        Point point = new Point(1,1,Duration.ONE, category_1, video)
        pointDAO.create(point)

        when:
        pointDAO.delete(point)

        then:
        pointDAO.findAll().size() == 0
    }

    def "add invalid point (video is null)"(){
        setup:
        Point point = new Point(1,1,Duration.ONE, category_1, null)

        when:
        pointDAO.create(point)

        then:
        thrown Exception
    }

    def "add invalid point (category is null)"(){
        setup:
        Point point = new Point(1,1,Duration.ONE, null, video)

        when:
        pointDAO.create(point)

        then:
        thrown Exception
    }
}
