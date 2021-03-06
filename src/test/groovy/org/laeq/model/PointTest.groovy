package org.laeq.model

import com.fasterxml.jackson.databind.ObjectMapper
import javafx.util.Duration
import org.laeq.model.dao.EntityGenerator
import spock.lang.Specification

class PointTest extends Specification {
    def "serialization "(){
        setup:
        User user = new User(1, 'firstName', 'lastName', 'email', false)
        Collection collection = new Collection(1, 'collection', false)
        Category category = new Category(1, "test", "test", "test" ,"A")
        collection.addCategory(category)
        Video video = new Video('path', Duration.ONE, collection, user)
        UUID id = UUID.randomUUID();
        Point point = new Point(id, 10,10, Duration.ONE, category, video)

        when:
        String result = new ObjectMapper().writeValueAsString(point)
        String expected = '{"id":"' + id + '","x":10.0,"y":10.0,"start":1.0,"category":1}'

        then:
        result == expected
    }

    def "deserialize" () {
        setup:
        UUID id = UUID.randomUUID();
        String json = '{"id":"' + id + '","x":10.0,"y":10.0,"start":1.0,"category":1}'

        ObjectMapper mapper = new ObjectMapper()

        when:
        Point point = mapper.readValue(json, Point.class)
        Category category = new Category()
        category.setId(1)

        then:
        point.getId().equals(id)
        point.getStart().equals(Duration.millis(1.0))
        point.getCategory().equals(new Category(1))
    }

    def "compare same point"() {
        setup:
        Category category = EntityGenerator.createCategory('A')
        Video video = EntityGenerator.createVideo()
        Point pt1 = new Point(1,0,Duration.ONE, category, video)

        expect:
        pt1.compareTo(pt1) == 0
    }


    def "compare two points"() {
        setup:
        Category category = EntityGenerator.createCategory('A')
        Video video = EntityGenerator.createVideo()
        Point pt1 = new Point(1,0,Duration.ONE, category, video)
        Point pt2 = new Point(0,0,Duration.ONE, category, video)

        expect:
        pt1.compareTo(pt2) == 0
    }

    def "TestEquals"() {
        setup:
        Category category = EntityGenerator.createCategory('A')
        Video video = EntityGenerator.createVideo()
        UUID id = UUID.randomUUID()
        Point pt1 = new Point(id,0,0,Duration.ONE, category, video)

        expect:
        pt1.equals(pt1) == true
    }

    def "2 points same uuid are equals"() {
        setup:
        Category category = EntityGenerator.createCategory('A')
        Video video = EntityGenerator.createVideo()
        UUID id = UUID.randomUUID()
        Point pt1 = new Point(id,0,0,Duration.ONE, category, video)
        Point pt2 = new Point(id,0,0,Duration.ONE, category, video)

        expect:
        pt1.equals(pt2) == true
    }

    def "2 points with diferent uuid are not equals"() {
        setup:
        Category category = EntityGenerator.createCategory('A')
        Video video = EntityGenerator.createVideo()
        Point pt1 = new Point(UUID.randomUUID(), 1,0,Duration.ONE, category, video)
        Point pt2 = new Point(UUID.randomUUID(), 0,0,Duration.ZERO, category, video)

        expect:
        pt1.equals(pt2) == false
    }
}
