package org.laeq.db


import org.laeq.model.*

class PointDAOTest extends AbstractDAOTest {
    PointDAO repository;
    Collection categoryCollection
    User user

//    def setup() {
//        repository = new PointDAO(manager, "point_id")
//        collection = new Collection(1, "mock", false)
//        user = new User(1, "test", "test", "test")
//    }
//
//    def "test insertion"() {
//        setup:
//        try{
//            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
//        } catch (Exception e){
//            println e
//        }
//
//        User user = new User(1, 'luck', 'skywalker', 'luke@maytheforcebewithyou.com')
//        Category category = new Category(1,  'Moving truck', 'icons/icon1.png', '#00000000','A')
//        Video video = new Video(1, 'path/to/video1.mp4', Duration.millis(12345.00), user, collection)
//
//
//        Point point = new Point(12.0, 12.0, Duration.seconds(1200), video, category)
//
//        when:
//        repository.insert(point)
//
//        then:
//        point.getId() == 11
//        repository.count() == 11
////        repository.findByVideo(video, user).size() == 6
//    }
//
//    def "test get next id"() {
//        when:
//        repository.getNextValue()
//        repository.getNextValue()
//        int result = repository.getNextValue()
//
//        then:
//        result == 3
//    }
//
//    def "test findAll but empty"() {
//        when:
//        def result = repository.findAll()
//
//        then:
//        result.size() == 0
//    }
//
//
//    def "test count"() {
//        setup:
//        try{
//            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
//        } catch (Exception e){
//            println e
//        }
//
//        when:
//        def result = repository.count()
//
//        then:
//        result == 10
//    }
//
//    def "test findAll"() {
//        setup:
//        try{
//            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
//        } catch (Exception e){
//            println e
//        }
//
//        when:
//        def result = repository.findAll()
//
//        then:
//        result.size() == 0
//    }
//
//    def "test findByVideoAndUser"() {
//        setup:
//        try{
//            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
//        } catch (Exception e){
//            println e
//        }
//
//        def user = new User(1, "mock", "mock", "mock@email.mock")
//        def video = new Video(1, "/path/to/video.mp4", Duration.millis(60000), collection)
//
//        when:
//        def result = repository.findByVideo(video, user)
//
//
//        then:
//        result.size() == 5
//        result.collect{it.id} == [2, 4, 3, 5, 1]
//        result.collect{it.category.icon} == ["icons/icon2.png", "icons/icon1.png", "icons/icon1.png", "icons/icon3.png", "icons/icon1.png"]
//    }
//
//    def "test findByVideoAndUser 2"() {
//        setup:
//        try{
//            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
//        } catch (Exception e){
//            println e
//        }
//
//        def user = new User(2, "mock", "mock", "mock@email.mock")
//        def video = new Video(1, "/path/to/video.mp4", Duration.millis(60000), collection)
//
//        when:
//        def result = repository.findByVideo(video, user)
//
//
//        then:
//        result.size() == 3
//        result.collect{it.id} == [9, 10, 8]
//        result.collect{it.category.icon} == ["icons/icon2.png", "icons/icon3.png", "icons/icon1.png"]
//    }
//
//
//    def "test delete"(){
//        setup:
//        try{
//            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
//        } catch (Exception e){
//            println e
//        }
//
//        User user = new User(1, 'luck', 'skywalker', 'luke@maytheforcebewithyou.com')
//        Category category = new Category(1,  'Moving truck', 'icons/icon1.png', 'A')
//        Video video = new Video(1, 'path/to/video1.mp4', Duration.millis(12345.00), collection)
//
//
//        Point point = new Point(1, 12.0, 12.0, Duration.seconds(1200), video, user, category)
//
//        when:
//        repository.delete(point)
//
//        then:
//        notThrown DAOException
//    }
//
//    def "test delete does not exists"(){
//        setup:
//        try{
//            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
//        } catch (Exception e){
//            println e
//        }
//
//        User user = new User(1, 'luck', 'skywalker', 'luke@maytheforcebewithyou.com')
//        Category category = new Category(1,  'Moving truck', 'icons/icon1.png', 'A')
//        Video video = new Video(1, 'path/to/video1.mp4', Duration.millis(12345.00), collection)
//
//
//        Point point = new Point(-1, 12.0, 12.0, Duration.seconds(1200), video, user, category)
//
//        when:
//        repository.delete(point)
//
//        then:
//        thrown DAOException
//    }
}