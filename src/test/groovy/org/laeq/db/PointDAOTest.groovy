package org.laeq.db

import javafx.util.Duration
import org.laeq.model.*

class PointDAOTest extends AbstractDAOTest {
    PointDAO repository
    Collection collection
    User user

    def setup() {
        repository = new PointDAO(manager)
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
        Video video = new Video(1, 'path/to/video1.mp4', Duration.millis(12345.00), user, collection)


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

    def "test findByVideo"() {
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        def user = new User(1, "mock", "mock", "mock@email.mock")
        Video video = new Video(2, "/path/to/video.mp4", Duration.millis(60000), user, collection)

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
        Video video = new Video(1, 'path/to/video1.mp4', Duration.millis(12345.00), user, collection)

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
        Video video = new Video(1, 'path/to/video1.mp4', Duration.millis(12345.00), user, collection)


        Point point = new Point(-1, 12.0, 12.0, Duration.seconds(1200), video, category)

        when:
        repository.delete(point)

        then:
        thrown DAOException
    }
}