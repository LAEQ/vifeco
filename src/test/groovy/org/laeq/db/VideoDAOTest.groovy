package org.laeq.db

import javafx.util.Duration
import org.laeq.model.CategoryCollection
import org.laeq.model.User
import org.laeq.model.Video

class VideoDAOTest extends AbstractDAOTest {
    VideoDAO repository
    CategoryCollection categoryCollection

    def setup() {
        repository = new VideoDAO(manager, VideoDAO.sequence_name)
        def result = false

        try{
            result = manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        if(! result)
            throw new Exception("VideoDAOTest: cannot load the fixtures")
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
        Video expexted = generateVideo(5, "path/to/video/name.mp4")

        then:
        video == expexted
    }

    Video generateVideo(String path){
        return new Video(path, Duration.millis(3600000),
                new User(1, "test", "test", "test"),
                new CategoryCollection(1, "test", false)
        )
    }

    Video generateVideo(int id, String path){
        return new Video(id, path, Duration.millis(3600000),
                new User(1, "test", "test", "test"),
                new CategoryCollection(1, "test", false)
        )
    }


    def "test findAll"(){
        setup:
        Video video1 = generateVideo("path/to/video/name.mp4")
        Video video2 = generateVideo("path/to/video/name2.mp4")
        Video video3 = generateVideo("path/to/video/name3.mp4")


        when:
        def result = repository.findAll()
        Video video = result.find{ it.id == 1}

        then:
        result.size() == 4
        video.total == 8
        video.user.id == 1
        video.user.firstName == "Luck"
        video.user.lastName == "Skywalker"
        video.categoryCollection.id == 1
        video.categoryCollection.name == "Default"
    }

    def "test findAll but empty"() {
        when:
        def result = repository.findAll()

        Video expected = generateVideo(1, "path/to/video/name.mp4")

        then:
        result.size() == 4

    }

    def "test delete an existing video"() {
        Video video = generateVideo(1, "path/to/video.mp4")

        when:
        repository.delete(video)

        then:
        notThrown DAOException
    }

    def "test delete an unknown video" (){

        def video = generateVideo(-1, "path/to/video.mp4")

        when:
        repository.delete(video)

        then:
        thrown DAOException
    }
}



