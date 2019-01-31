package org.laeq.db

import org.laeq.model.Category
import org.laeq.model.Video
import javafx.util.Duration;

class VideoDAOTest extends AbstractDAOTest {
    DAOInterface<Video> repository

    def setup() {
        repository = new VideoDAO(manager, "category_id")
    }

    def "test get next id"() {
        when:
        repository.getNextValue()
        repository.getNextValue()
        int result = repository.getNextValue()

        then:
        result == 3
    }

    def "test insertion"() {
        setup:
        Video video = new Video("path/to/video/name.mp4", Duration.seconds(3000))

        when:
        repository.findAll();
        repository.insert(video)

        then:
        video == new Video(1, "path/to/video/name.mp4", Duration.seconds(3600))
    }

    def "test findAll"(){
        setup:
        Video video1 = new Video("path/to/video/name.mp4", Duration.seconds(3000))
        Video video2 = new Video("path/to/video/name2.mp4", Duration.seconds(3000))
        Video video3 = new Video("path/to/video/name3.mp4", Duration.seconds(3000))

        repository.insert(video1)
        repository.insert(video2)
        repository.insert(video3)

        when:
        def result = repository.findAll()

        then:
        result.size() == 3
    }

    def "test findAll but empty"() {
        when:
        def result = repository.findAll()

        then:
        result.size() == 0
    }

    def "test delete an existing video"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql").toURI().getPath())
        } catch (Exception e){
            println e
        }

        Video video = new Video(1, "path/to/video.mp4", Duration.seconds(3600))

        when:
        repository.delete(video)

        then:
        notThrown DAOException
    }

    def "test delete an unknown video" (){
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql").toURI().getPath())
        } catch (Exception e){
            println e
        }

        def video = new Video(-1, "path/to/video.mp4", Duration.seconds(3600))

        when:
        repository.delete(video)

        then:
        thrown DAOException
    }
}



