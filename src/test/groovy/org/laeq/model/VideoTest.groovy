package org.laeq.model

import com.fasterxml.jackson.databind.ObjectMapper
import javafx.util.Duration
import spock.lang.Specification

class VideoTest extends Specification {
    def "serialize"(){
        setup:
        Integer userId = 1
        User user = new User(userId, "test", "test", false)
        Collection categoryCollection = new Collection(1, "collection 1", false)
        Category category1 = new Category(1, "category 1", "icon 1", "color 1", "A")
        Category category2 = new Category(2, "category 2", "icon 2", "color 2", "B")
        categoryCollection.addCategory(category1)
        categoryCollection.addCategory(category2)

        UUID videoId = UUID.randomUUID()
        Video video = new Video("path/to/video.mp4", Duration.millis(10000), categoryCollection, user)
        video.setId(videoId)

        UUID pt1_id = UUID.randomUUID()
        UUID pt2_id = UUID.randomUUID()

        Point point1 = new Point(pt1_id, 10, 11, Duration.millis(1000),  category1, video)
        Point point2 = new Point(pt2_id, 11, 10, Duration.millis(2000),  category2, video)

        video.getPoints().add(point1)
        video.getPoints().add(point2)

        when:
        String result = new ObjectMapper().writeValueAsString(video)
        String expected = "{'id':'${videoId}','path':'video.mp4','duration':10000.0," +
                "'user':{'id':1,'firstName':'test','lastName':'test'}," +
                "'collection':" +
                    "{'id':1,'name':'collection 1'," +
                    "'categories':[" +
                    "{'id':1,'name':'category 1'}," +
                    "{'id':2,'name':'category 2'}" +
                    "]}," +
                "'points':[" +
                    "{'id':'${pt1_id}','x':10.0,'y':11.0,'start':1000.0,'category':1}," +
                    "{'id':'${pt2_id}','x':11.0,'y':10.0,'start':2000.0,'category':2}" +
                "]}"

        then:
        result == expected.replace("'", "\"")
    }

    def "deserialization" (){
        setup:
        String json = getClass().classLoader.getResource("export/export_1.json").text

        UUID videoId = UUID.randomUUID()
        UUID pt1_id = UUID.randomUUID()

        json = json.replaceAll("videoId", videoId.toString())
        json = json.replaceAll("ptId", pt1_id.toString())

        ObjectMapper mapper = new ObjectMapper()

        Collection expectedCollection = new Collection(1,"Default", Boolean.TRUE)
        expectedCollection.addCategory(new Category(1, "Moving car"))
        expectedCollection.addCategory(new Category(2, "Moving bike"))

        when:
        Video result = mapper.readValue(json, Video.class)

        then:
        result.path == 'exported_video.wav'
        result.duration.equals(Duration.millis(258573.333334))

        result.collection == expectedCollection
        result.points.size() == 1
    }
}
