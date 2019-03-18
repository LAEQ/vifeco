package org.laeq.video

import org.laeq.db.*
import org.laeq.model.Point
import org.laeq.model.Video

class ImportServiceTest extends AbstractDAOTest {
    VideoDAO videoDAO
    UserDAO userDAO
    CollectionDAO collectionDAO
    PointDAO pointDAO
    private ImportService service
    private String json


    def setup(){
        videoDAO = new VideoDAO(manager, collectionDAO, userDAO)
        pointDAO = new PointDAO(manager)
        service = new ImportService(videoDAO, pointDAO)
    }

    def "execute with a json string"() {
        setup:
        try{
            json = getClass().classLoader.getResource("export/export_1.json").text
        } catch(Exception e){
            println e
        }

        when:
        Video result = service.execute(json)

        then:
        result instanceof Video == true
    }

    def "import json file and save to database"() {
        setup:

        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        File file
        try{
            file = new File(getClass().classLoader.getResource("export/export_1.json").getFile())
        } catch(Exception e){
            println e
        }

        when:
        boolean result = service.execute(file)
        Set<Video> videos = videoDAO.findAll()

        Video expected = videos.find { it.path == 'exported_video.wav'}
        SortedSet<Point> points = pointDAO.findByVideo(expected)

        then:
        result == true
        expected instanceof Video
        points.size() == 3
    }

    def "import json file with invalid collection"() {
        setup:

        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        File file
        try{
            file = new File(getClass().classLoader.getResource("export/export_invalid_collection.json").getFile())
        } catch(Exception e){
            println e
        }

        when:
        boolean result = service.execute(file)
        Set<Video> videos = videoDAO.findAll()

        Video expected = videos.find { it.path == 'exported_video.wav'}
        SortedSet<Point> points = pointDAO.findByVideo(expected)

        then:
        thrown DAOException
    }
}
