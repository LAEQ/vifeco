package org.laeq.db

import javafx.util.Duration
import org.laeq.model.Point
import org.laeq.model.User
import org.laeq.model.Video

import static org.laeq.model.Point.*

class PointDAOTest extends AbstractDAOTest {
    def repository;

    def setup() {
        repository = new PointDAO(manager, "point_id")
    }

    def "test get next id"() {
        when:
        repository.getNextValue()
        repository.getNextValue()
        int result = repository.getNextValue()

        then:
        result == 3
    }

    def "test findAll but empty"() {
        when:
        def result = repository.findAll()

        then:
        result.size() == 0
    }

    def "test findAll"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql").toURI().getPath())
        } catch (Exception e){
            println e
        }


        when:
        def result = repository.findAll()

        then:
        result.size() == 0
    }

    def "test findByVideoAndUser"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql").toURI().getPath())
        } catch (Exception e){
            println e
        }

        def user = new User(1, "mock", "mock", "mock@email.mock")
        def video = new Video(1, "/path/to/video.mp4", Duration.millis(60000))

        when:
        def result = repository.findByVideoAndUser(video, user)

        then:
        result.size() == 5
        result.collect{it.id} == [2, 4, 3, 5, 1]
        result.collect{it.category.icon} == ["icons/icon2.png", "icons/icon1.png", "icons/icon1.png", "icons/icon3.png", "icons/icon1.png"]

    }
}