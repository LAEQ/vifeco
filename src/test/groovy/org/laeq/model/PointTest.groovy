package org.laeq.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import javafx.util.Duration
import org.laeq.model.serializer.PointDeserializer
import spock.lang.Specification

class PointTest extends Specification {
    def "serialization "(){
        setup:
        User user = new User(1, "test", "test", "test")
        Collection categoryCollection = new Collection()
        Point video = new Point(1, "test", Duration.millis(1000), user, categoryCollection)
        Category category = new Category(1, "test", "test", "test" ,"A")
        Point point = new Point(1, 10,10, Duration.millis(10000), video, category)

        when:
        String result = new ObjectMapper().writeValueAsString(point)

        String expected = "{'id':1,'x':10.0,'y':10.0,'categoryId':1,'startDouble':10000.0,'videoId':'$video.uuid'}"

        then:
        result == expected.replace("'", "\"")
    }

    def "deserialize" () {
        setup:
        String json = ' {\n' +
                '      "id": 1,\n' +
                '      "x": 0.4155672823218997,\n' +
                '      "y": 0.3915441176470588,\n' +
                '      "categoryId": 1,\n' +
                '      "startDouble": 10.0\n' +
                '    }'

        PointDeserializer deserializer = new PointDeserializer()
        ObjectMapper mapper = new ObjectMapper()
        SimpleModule module = new SimpleModule()
        module.addDeserializer(Point.class, deserializer)
        mapper.registerModule(module)

        when:
        Point result = mapper.readValue(json, Point.class)
        Category category = new Category()
        category.setId(1)

        then:
        result.id == 1
        result.getCategory() == category
        result.getStartDouble() == 10d
        result.getX() == 0.4155672823218997
        result.getY() == 0.3915441176470588
    }
}
