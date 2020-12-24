package org.laeq.model.dao

import javafx.util.Duration
import org.laeq.db.HibernateUtil
import org.laeq.model.*
import spock.lang.Specification

class VideoPointMappingTest extends Specification {
    VideoDAO videodao;
    CollectionDAO colDAO
    CategoryDAO catDAO
    UserDAO userDAO
    PointDAO pointDAO
    Category category_1, category_2
    Collection collection
    User user1, user2
    Video video1, video2
    Integer id1, id2
    List<Point> list1, list2


    void setup() {
        videodao = new VideoDAO(new HibernateUtil('hibernate.cfg.xml'))
        catDAO = new CategoryDAO(new HibernateUtil('hibernate.cfg.xml'))
        colDAO = new CollectionDAO(new HibernateUtil('hibernate.cfg.xml'))
        userDAO = new UserDAO(new HibernateUtil('hibernate.cfg.xml'))
        pointDAO = new PointDAO(new HibernateUtil('hibernate.cfg.xml'))

        user1 = EntityGenerator.createUser()
        user1.setDefault(Boolean.FALSE)
        userDAO.create(user1)

        user2 = EntityGenerator.createUser()
        user2.setDefault(Boolean.FALSE)
        userDAO.create(user2)

        category_1 = EntityGenerator.createCategory("A")
        category_2 = EntityGenerator.createCategory("B")

        collection = new Collection("mock col 1")
        collection.addCategory(category_1)
        collection.addCategory(category_2)

        catDAO.create(category_1)
        catDAO.create(category_2)
        colDAO.create(collection)

        video1 = new Video('mock/path2', Duration.millis(1000), collection, user1)
        videodao.create(video1)
        id1 = video1.getId()

        video2 = new Video('mock/path2', Duration.millis(1000), collection, user2)
        videodao.create(video2)
        id2 = video2.getId()

        list1 = new ArrayList<>();

        1.upto(20, {
            Point point1 = new Point(it, it, Duration.millis(Math.random() * 10000), category_1)
            Point point2 = new Point(it, it, Duration.millis(Math.random() * 10000), category_2)

            video1.addPoint(point1)
            video1.addPoint(point2)
            pointDAO.create(point1)
            pointDAO.create(point2)

            list1.push(point1)
            list1.push(point2)
        })

        list2 = new ArrayList<>();

        1.upto(10, {
            Point point1 = new Point(it, it, Duration.millis(Math.random() * 10000), category_1)
            Point point2 = new Point(it, it, Duration.millis(Math.random() * 10000), category_2)

            video2.addPoint(point1)
            video2.addPoint(point2)
            pointDAO.create(point1)
            pointDAO.create(point2)

            list2.push(point1)
            list2.push(point2)
        })

    }

    void cleanup() {

    }

    def "delete a video cascading for points"(){
        when:
        videodao.delete(video1)
        def fetch2 = videodao.findOneById(id2)
        def fetch1 = videodao.findOneById(id1)

        then:
        fetch2.getPoints().size() == 20
        fetch1 == null
        pointDAO.findAll().size() == 20
    }

    def "cannot delete a user with videos."(){
        when:
        userDAO.delete(user1)

        then:
        thrown Exception
    }

    def "cannot delete a category with points."(){
        when:
        collection.removeCategory(category_1)
        colDAO.create(collection)
        catDAO.delete(category_1)

        then:
        thrown Exception
    }
}
