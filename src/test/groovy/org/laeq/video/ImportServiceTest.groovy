package org.laeq.video

import org.laeq.ImportService
import org.laeq.db.AbstractDAOTest
import org.laeq.db.DAOException
import org.laeq.model.Point
import spock.lang.Ignore

@Ignore
class ImportServiceTest extends AbstractDAOTest {
    private ImportService service
    private String json


    def setup(){
        service = new ImportService()
    }


    def "execute with a json string"() {
        setup:
        try{
            json = getClass().classLoader.getResource("export/export_1.json").text
        } catch(Exception e){
            println e
        }

        when:
        Point result = service.execute(json)

        then:
        result instanceof Point == true
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

        Point expected = videos.find { it.path == '/path/export/exported_video.wav'}
        SortedSet<Point> points = pointDAO.findByVideo(expected)

        then:
        result == true
        expected instanceof Point
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
        Set<Point> videos = videoDAO.findAll()

        Point expected = videos.find { it.path == 'exported_video.wav'}
        SortedSet<Point> points = pointDAO.findByVideo(expected)

        then:
        thrown DAOException
    }
}
