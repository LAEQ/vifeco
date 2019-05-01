package org.laeq.model.statistic

import javafx.util.Duration
import org.laeq.model.Point
import spock.lang.Specification

class EdgeTest extends Specification {
    def "Equals"() {
        given:
        Vertex start = new Vertex(new Point(1, Duration.millis(1000)))
        Vertex end = new Vertex(new Point(2, Duration.millis(1000)))
        Edge edge1 = new Edge(start, end)
        Edge edge2 = new Edge(end, start)

        expect:
        edge1.equals(edge2) == true
    }

    def "test set"() {
        setup:
        Vertex start = new Vertex(new Point(1, Duration.millis(1000)))
        Vertex end = new Vertex(new Point(2, Duration.millis(1000)))
        Edge edge1 = new Edge(start, end)
        Edge edge2 = new Edge(end, start)

        when:
        Set<Edge> set = new HashSet<>()
        set.add(edge1)
        set.add(edge1)
        set.add(edge2)

        then:
        set.size() == 1
    }
}
