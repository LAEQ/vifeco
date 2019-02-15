package org.laeq.db

import javafx.util.Duration
import org.laeq.model.CategoryCollection
import org.laeq.model.Video

class VideoDAOTest extends AbstractDAOTest {
    DAOInterface<Video> repository

    CategoryCollection categoryCollection;

    def setup() {
        repository = new VideoDAO(manager, "category_id")

        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        categoryCollection = new CategoryCollection(1, "test", false);
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
        Video video = new Video("path/to/video/name.mp4", Duration.millis(3600000), categoryCollection)

        when:
        repository.insert(video)

        then:
        video == new Video(5, "path/to/video/name.mp4", Duration.millis(3600000), categoryCollection)
    }

    def "test findAll"(){
        setup:
        Video video1 = new Video("path/to/video/name.mp4", Duration.millis(3600000), categoryCollection)
        Video video2 = new Video("path/to/video/name2.mp4", Duration.millis(3600000), categoryCollection)
        Video video3 = new Video("path/to/video/name3.mp4", Duration.millis(3600000), categoryCollection)

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

        Video video = new Video(1, "path/to/video.mp4", Duration.millis(3600000), categoryCollection)

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

        def video = new Video(-1, "path/to/video.mp4", Duration.millis(3600000), categoryCollection)

        when:
        repository.delete(video)

        then:
        thrown DAOException
    }
}



