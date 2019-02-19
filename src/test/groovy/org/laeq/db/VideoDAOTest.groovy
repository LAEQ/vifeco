package org.laeq.db

import javafx.util.Duration
import org.laeq.model.CategoryCollection
import org.laeq.model.User
import org.laeq.model.Video

class VideoDAOTest extends AbstractDAOTest {
    DAOInterface<Video> repository

    CategoryCollection categoryCollection
    User user

    def setup() {
        repository = new VideoDAO(manager, "category_id")

        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        categoryCollection = new CategoryCollection(1, "test", false)
        user = new User(1,"test", "test", "test")

    }

    def "test get next id"() {
        when:
        repository.getNextValue()
        repository.getNextValue()
        int result = repository.getNextValue()

        then:
        result == 7
    }

    def "test insertion"() {
        setup:
        Video video = generateVideo("path/to/video/name.mp4")

        when:
        repository.insert(video)

        then:
        video == generateVideo(5, "path/to/video/name.mp4")
    }

    Video generateVideo(String path){
        return new Video(path, Duration.millis(3600000), user, categoryCollection)
    }
    Video generateVideo(int id, String path){
        return new Video(id, path, Duration.millis(3600000), user, categoryCollection)
    }


    def "test findAll"(){
        setup:
        Video video1 = generateVideo("path/to/video/name.mp4")
        Video video2 = generateVideo("path/to/video/name2.mp4")
        Video video3 = generateVideo("path/to/video/name3.mp4")

        repository.insert(video1)
        repository.insert(video2)
        repository.insert(video3)

        when:
        def result = repository.findAll()

        then:
        result.size() == 7
    }

    def "test findAll but empty"() {
        when:
        def result = repository.findAll()

        then:
        result.size() == 4
    }

    def "test delete an existing video"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        Video video = generateVideo(1, "path/to/video.mp4")

        when:
        repository.delete(video)

        then:
        notThrown DAOException
    }

    def "test delete an unknown video" (){
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        def video = generateVideo(-1, "path/to/video.mp4")

        when:
        repository.delete(video)

        then:
        thrown DAOException
    }
}



