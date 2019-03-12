package org.laeq.db

import javafx.util.Duration
import org.laeq.model.Collection
import org.laeq.model.User
import org.laeq.model.Video

class VideoDAOTest extends AbstractDAOTest {
    VideoDAO repository
    UserDAO userDAO
    CollectionDAO collectionDAO

    def setup() {
        userDAO = new UserDAO(manager)
        collectionDAO = new CollectionDAO(manager)
        repository = new VideoDAO(manager, collectionDAO, userDAO)
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
                new Collection(1, "test", false)
        )
    }

    Video generateVideo(int id, String path){
        return new Video(id, path, Duration.millis(3600000),
                new User(1, "test", "test", "test"),
                new Collection(1, "test", false)
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
        video.collection.id == 1
        video.collection.name == "Default"
    }

    def "test delete an existing video"() {
        Video video = generateVideo(1, "path/to/video.mp4")

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
        Video video = generateVideo(1, "path/to/video.mp4")
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
        Video video = generateVideo(1, "path/to/video.mp4")
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
        Video video = generateVideo(1, "path/to/video.mp4")
        def user = new User(2, "test", "test", "test")


        when:
        repository.updateUser(video, user)
        def videoUpdated = repository.findAll().find { it.id == 1}

        then:
        videoUpdated.user.id == 2
        noExceptionThrown()
    }

    def "update video collection"(){
        Video video = generateVideo(1, "path/to/video.mp4")
        def collection = new Collection(2, "test", false)

        when:
        repository.updateCollection(video, collection)
        def videoUpdated = repository.findAll().find { it.id == 1}

        then:
        videoUpdated.collection.id == 2
        noExceptionThrown()
    }
}



