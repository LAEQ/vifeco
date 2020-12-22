package org.laeq.db

import javafx.util.Duration
import org.laeq.model.Collection
import org.laeq.model.Point
import org.laeq.model.User
import org.laeq.model.Video

class VideoDAOTest extends AbstractDAOTest {
    VideoDAO repository
    UserDAO userDAO
    CollectionDAO collectionDAO

    def setup() {
        userDAO = new UserDAO(manager)
        collectionDAO = new CollectionDAO(manager)
        repository = new VideoDAO(manager, collectionDAO)
        def result = false

        try{
            result = manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        if(! result)
            throw new Exception("VideoDAOTest: cannot load the fixtures")
    }

    def "test insertion"() {
        setup:
        Point video = generateVideo("path/to/video/name.mp4")

        when:
        repository.insert(video)
        Point expexted = generateVideo(5, "path/to/video/name.mp4")

        then:
        video.id == 5
    }

    Point generateVideo(String path){
        return new Point(path, Duration.millis(3600000),
                new User(1, "test", "test", "test"),
                new Collection(1, "test", false)
        )
    }

    Point generateVideo(int id, String path){
        return new Point(id, path, Duration.millis(3600000),
                new User(1, "test", "test", "test"),
                new Collection(1, "test", false)
        )
    }

    def "test findAll"(){
        setup:
        Point video1 = generateVideo("path/to/video/name.mp4")
        Point video2 = generateVideo("path/to/video/name2.mp4")
        Point video3 = generateVideo("path/to/video/name3.mp4")


        when:
        def result = repository.findAll()
        Point video = result.find{ it.id == 1}

        then:
        result.size() == 4
        video.total == 8
        video.user.id == 1
        video.user.firstName == "Luck"
        video.user.lastName == "Skywalker"
        video.collection.id == 1
        video.collection.name == "Default"
    }

    def "test delete an existing video"() {
        Point video = generateVideo(1, "path/to/video.mp4")

        when:
        repository.delete(video)

        then:
        repository.findAll().size() == 3
        notThrown DAOException
    }

    def "test delete an unknown video" (){
        def video = generateVideo(-1, "path/to/video.mp4")

        when:
        repository.delete(video)

        then:
        thrown DAOException
    }

    def "update video user and category"(){
        Point video = generateVideo(1, "path/to/video.mp4")
        def user = new User(2, "test", "test", "test")
        def collection = new Collection(2, "test", false)
        video.setUser(user)
        video.setCollection(collection)

        when:
        repository.update(video)
        def videoUpdated = repository.findAll().find { it.id == 1}

        println videoUpdated

        then:
        noExceptionThrown()

    }

    def "update video"(){
        Point video = generateVideo(1, "path/to/video.mp4")
        def user = new User(2, "test", "test", "test")
        def collection = new Collection(2, "test", false)
        video.setUser(user)
        video.setCollection(collection)

        when:
        repository.update(video)
        def videoUpdated = repository.findAll().find { it.id == 1}


        then:
        videoUpdated.user.id == 2
        videoUpdated.collection.id == 2
        noExceptionThrown()

    }

    def "update video user"(){
        Point video = generateVideo(1, "path/to/video.mp4")
        def user = new User(2, "test", "test", "test")


        when:
        repository.updateUser(video, user)
        def videoUpdated = repository.findAll().find { it.id == 1}

        then:
        videoUpdated.user.id == 2
        noExceptionThrown()
    }

    def "update video collection"(){
        Point video = generateVideo(1, "path/to/video.mp4")
        def collection = new Collection(2, "test", false)

        when:
        repository.updateCollection(video, collection)
        def videoUpdated = repository.findAll().find { it.id == 1}

        then:
        videoUpdated.collection.id == 2

        noExceptionThrown()
    }
}



