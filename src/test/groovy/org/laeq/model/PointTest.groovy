package org.laeq.model

import com.fasterxml.jackson.databind.ObjectMapper
import javafx.util.Duration
import spock.lang.Specification

class PointTest extends Specification {
    def "serialization "(){
        setup:
        User user = new User(1, "test", "test", "test")
        CategoryCollection categoryCollection = new CategoryCollection()
        Video video = new Video(1, "test", Duration.millis(1000), user, categoryCollection)
        Category category = new Category(1, "test", "test", "test" ,"A")
        Point point = new Point(1, 10,10, Duration.millis(10000), video, category)

        when:
        String result = new ObjectMapper().writeValueAsString(point)

        String expected = '{"x":10.0,"y":10.0,"categoryId":1,"startDouble":10000.0}'

        then:
        result == expected
    }
}
