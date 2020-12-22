package org.laeq.db

import javafx.util.Duration
import org.laeq.model.*
import spock.lang.Ignore

class PointDAOTest extends AbstractDAOTest {
    PointDAO repository
    UserDAO userDAO
    VideoDAO videoDAO
    Collection collection
    User user

    def setup() {
        repository = new PointDAO(manager)
        userDAO = new UserDAO(manager)
        videoDAO = new VideoDAO(manager, new CollectionDAO(manager))
        collection = new Collection(1, "mock", false)
        user = new User(1, "test", "test", "test")
    }

    def "test insertion"() {
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        User user = new User(1, 'luck', 'skywalker', 'luke@maytheforcebewithyou.com')
        Category category = new Category(1,  'Moving truck', 'icons/icon1.png', '#00000000','A')
        Point video = new Point(1, 'path/to/video1.mp4', Duration.millis(12345.00), user, collection)


        Point point = new Point(12.0, 12.0, Duration.seconds(1200), video, category)

        when:
        repository.insert(point)

        then:
        point.getId() == 11
        repository.findByVideo(video).size() == 9
    }

    def "test count"() {
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        when:
        def result = repository.count()

        then:
        result == 10
    }

    def "test findAll"() {
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        when:
        def result = repository.findAll()

        then:
        result.size() == 0
    }

    def "test find previous"() {
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }
        Point point = new Point()
        Category category = new Category(1, 'test', 'test' ,'test', 'a')
        Point video = new Point(1, 'test', Duration.millis(1000), new User(), new Collection());

        point.setCategory(category)
        point.setVideo(video)
        point.setStart(Duration.millis(2223))

        when:
        def result = repository.findPrevious(point)

        then:
        result.getId() == 4
    }

    def "test find previous not exist"() {
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }
        Point point = new Point()
        Category category = new Category(1, 'test', 'test' ,'test', 'a')
        Point video = new Point(1, 'test', Duration.millis(1000), new User(), new Collection());

        point.setCategory(category)
        point.setVideo(video)
        point.setStart(Duration.millis(2219))

        when:
        def result = repository.findPrevious(point)

        then:
        result == null
    }

    def "test find next"() {
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }
        Point point = new Point()
        Category category = new Category(1, 'test', 'test' ,'test', 'a')
        Point video = new Point(1, 'test', Duration.millis(1000), new User(), new Collection());

        point.setCategory(category)
        point.setVideo(video)
        point.setStart(Duration.millis(3333))

        when:
        def result = repository.findNext(point)

        then:
        result.getId() == 1
    }

    def "test find next not exist"() {
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }
        Point point = new Point()
        Category category = new Category(1, 'test', 'test' ,'test', 'a')
        Point video = new Point(1, 'test', Duration.millis(1000), new User(), new Collection());

        point.setCategory(category)
        point.setVideo(video)
        point.setStart(Duration.millis(5555))

        when:
        def result = repository.findNext(point)

        then:
        result == null
    }

    def "test findByVideo"() {
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        def user = new User(1, "mock", "mock", "mock@email.mock")
        Point video = new Point(2, "/path/to/video.mp4", Duration.millis(60000), user, collection)

        when:
        def result = repository.findByVideo(video)


        then:
        result.size() == 2
        result.collect{it.id} == [6, 7]
    }

    def "test delete"(){
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        User user = new User(1, 'luck', 'skywalker', 'luke@maytheforcebewithyou.com')
        Category category = new Category(1,  'Moving truck', 'icons/icon1.png', '#FFFFFFFF','A')
        Point video = new Point(1, 'path/to/video1.mp4', Duration.millis(12345.00), user, collection)

        Point point = new Point(1, 12.0, 12.0, Duration.seconds(1200), video, category)

        when:
        repository.delete(point)


        then:
        repository.findByVideo(video).size() == 7
        notThrown DAOException
    }

    def "test delete does not exists"(){
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        User user = new User(1, 'luck', 'skywalker', 'luke@maytheforcebewithyou.com')
        Category category = new Category(1,  'Moving truck', 'icons/icon1.png', '#FFFFFFFF','A')
        Point video = new Point(1, 'path/to/video1.mp4', Duration.millis(12345.00), user, collection)


        Point point = new Point(-1, 12.0, 12.0, Duration.seconds(1200), video, category)

        when:
        repository.delete(point)

        then:
        thrown DAOException
    }

    def "delete point on updated collection"() {
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        when:
        Point video = new Point(1, 'path/to/video1.mp4', Duration.millis(12345.00), user, collection)
        collection = new Collection(2, "mock", false)
        video.setCollection(collection)

        def result = repository.updateOnCollectionChange(video)
        def points = repository.findByVideo(video)

        then:
        result == 4
        points.size() == 4
        points.collect { it.category.id }.sort() == [2,2,3,3]
    }

    @Ignore
    def "test cascade"() {
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        when:
        User user1 = userDAO.findById(2)

        userDAO.delete(user1)

        def users = userDAO.findAll()
        def videos = videoDAO.findAll()
        def total = repository.count()

        then:
        users.size() == 3
        videos.size() == 3
        total == 8
    }
}