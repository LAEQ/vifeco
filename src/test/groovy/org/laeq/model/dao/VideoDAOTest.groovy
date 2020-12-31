package org.laeq.model.dao

import javafx.util.Duration
import org.laeq.model.*
import spock.lang.Specification

class VideoDAOTest extends Specification {
    VideoDAO dao;
    CollectionDAO colDAO
    CategoryDAO catDAO
    UserDAO userDAO
    PointDAO pointDAO
    Category category_1, category_2
    Collection collection
    Collection collection2
    User user;


    void setup() {
        dao = new VideoDAO(new HibernateUtil('hibernate.cfg.xml'))
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

        collection2 = new Collection("mock col 2")
        collection2.addCategory(category_1)

        catDAO.create(category_1)
        catDAO.create(category_2)
        colDAO.create(collection)
        colDAO.create(collection2)
    }

    void cleanup() {

    }

    def "test one insertion"(){
        setup:
        Video video = new Video('mock/path', Duration.millis(1000), collection, user)

        when:
        dao.create(video)

        then:
        video.getId() != null
    }

    def "delete one video"(){
        setup:
        Video video = new Video('mock/path', Duration.millis(1000), collection, user)
        dao.create(video)

        when:
        dao.delete(video)

        then:
        notThrown Exception
    }

    def "delete one video for user with multiple videos"(){
        setup:
        Video video1 = new Video('mock/path', Duration.millis(1000), collection, user)
        Video video2 = new Video('mock/path', Duration.millis(1000), collection, user)
        dao.create(video1)
        dao.create(video2)

        when:
        dao.delete(video1)

        then:
        dao.findAll().size() == 1
    }

    def "add a point "(){
        setup:
        Video video1 = new Video('mock/path', Duration.millis(1000), collection, user)
        dao.create(video1)
        Point point = new Point(1.0, 1.0, Duration.ONE, category_1)
        video1.addPoint(point)

        when:
        pointDAO.create(point)

        then:
        dao.findAll().size() == 1
    }

    def "remove a point"(){
        setup:
        Video video1 = new Video('mock/path', Duration.millis(1000), collection, user)
        dao.create(video1)
        def id = video1.getId()

        1.upto(100, {
            Point point = new Point(it, it, Duration.millis(Math.random() * 10000), category_1)
            video1.addPoint(point)
            pointDAO.create(point)
        })

        Point point = new Point(100,100, Duration.millis(Math.random() * 10000), category_1)
        video1.addPoint(point)
        pointDAO.create(point)

        when:
        pointDAO.delete(point)
        Video video = dao.findOneByUUID(id)

        then:
        video.getPoints().size() == 100
    }

    def "multiple video remove few  points"(){
        setup:
        Video video1 = new Video('mock/path2', Duration.millis(1000), collection, user)
        dao.create(video1)
        def id = video1.getId()

        Video video2 = new Video('mock/path2', Duration.millis(1000), collection, user)
        dao.create(video2)
        def id2 = video2.getId()


        1.upto(20, {
            Point point1 = new Point(it, it, Duration.millis(Math.random() * 10000), category_1)
            Point point2 = new Point(it, it, Duration.millis(Math.random() * 10000), category_2)

            video1.addPoint(point1)
            video1.addPoint(point2)
            pointDAO.create(point1)
            pointDAO.create(point2)
        })

        1.upto(10, {
            Point point1 = new Point(it, it, Duration.millis(Math.random() * 10000), category_1)
            Point point2 = new Point(it, it, Duration.millis(Math.random() * 10000), category_2)

            video2.addPoint(point1)
            video2.addPoint(point2)
            pointDAO.create(point1)
            pointDAO.create(point2)
        })

        when:
        Video video = dao.findOneByUUID(id)

        then:
        video.getPoints().size() == 40
    }

    def "update video collection must delete obsolete points"(){
        setup:
        Video video1 = new Video('mock/path2', Duration.millis(1000), collection, user)
        dao.create(video1)
        Point pt1 = new Point(1,1,Duration.ONE, category_1)
        Point pt2 = new Point(1,1,Duration.millis(2.0), category_2)

        video1.addPoint(pt1)
        video1.addPoint(pt2)

        dao.create(video1)

        def id = video1.getId()

        when:
        video1.updateCollection(collection2)

        then:
        video1.getPoints().size() == 0
    }
}
