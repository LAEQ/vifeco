package org.laeq.model

import com.fasterxml.jackson.databind.ObjectMapper
import javafx.util.Duration
import spock.lang.Specification

class VideoTest extends Specification {
    def "serialize"(){
        setup:
        User user = new User(1, "test", "test", "test")
        Collection categoryCollection = new Collection(1, "collection 1", false)
        Category category1 = new Category(1, "category 1", "icon 1", "color 1", "A")
        Category category2 = new Category(2, "category 2", "icon 2", "color 2", "B")
        categoryCollection.addCategory(category1)
        categoryCollection.addCategory(category2)

        Video video = new Video(1, "path/to/video.mp4", Duration.millis(10000), user, categoryCollection)

        Point point1 = new Point(1, 10, 11, Duration.millis(1000),  video, category1)
        Point point2 = new Point(2, 11, 10, Duration.millis(2000),  video, category2)

        video.pointSet.add(point1)
        video.pointSet.add(point2)

        when:
        String result = new ObjectMapper().writeValueAsString(video)
        String expected = '{"path":"path/to/video.mp4","user":{"id":1,"firstName":"test","lastName":"test","email":"test","isActive":false},"duration":10000.0,' +
                '"collection":{"id":1,"name":"collection 1","isDefault":false,' +
                '"categorySet":[{"id":1,"name":"category 1","icon":"icon 1","color":"color 1","shortcut":"A"},' +
                '{"id":2,"name":"category 2","icon":"icon 2","color":"color 2","shortcut":"B"}]},' +
                '"pointSet":[{"x":10.0,"y":11.0,"categoryId":1,"startDouble":1000.0},' +
                '{"x":11.0,"y":10.0,"categoryId":2,"startDouble":2000.0}]}'

        then:
        result == expected
    }
}
