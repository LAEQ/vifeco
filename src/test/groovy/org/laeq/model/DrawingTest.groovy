package org.laeq.model

import com.fasterxml.jackson.databind.ObjectMapper
import javafx.geometry.Point2D
import org.laeq.editor.DrawingType
import spock.lang.Specification

class DrawingTest extends Specification {
    def "serialization" () {
        setup:
        Drawing draw = new Drawing(DrawingType.LINE, "#FF00FF", new Point2D(10.1, 11.2), new Point2D(12.1, 12.2))
        draw.setId(1)

        when:
        String result = new ObjectMapper().writeValueAsString(draw)
        String expected = '{"id":1,"type":"LINE","color":"#FF00FF","start":"10.100000;11.200000","end":"12.100000;12.200000"}'

        then:
        result == expected
    }

    def "deserialization" () {
        setup:
        String json = '{"id":1,"type":"RECTANGLE","color":"#FF00FF","start":"10.100000;11.200000","end":"12.100000;12.200000"}'
        Drawing expected = new Drawing(DrawingType.RECTANGLE, "#FF00FF", new Point2D(10.1, 11.2), new Point2D(12.1, 12.2))
        expected.setId(1)

        when:
        Drawing result = new ObjectMapper().readValue(json, Drawing.class)


        then:
        result == expected
    }
}
