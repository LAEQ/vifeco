package org.laeq.model.statistic

import org.laeq.model.Point
import spock.lang.Specification

class GraphTest extends Specification {
    def "GetDistinctEdges"() {
        Graph graph = new Graph()
        Point point1 = new Point(1)
        Point point2 = new Point(2)
        Point point3 = new Point(3)

        graph.addVertex(point1)
        graph.addVertex(point2)
        graph.addVertex(point3)

        graph.addEdges(point1, point2)
        graph.addEdges(point1, point3)
        graph.addEdges(point2, point1)
        graph.addEdges(point3, point1)

        def result = graph.getDistinctEdges()

        expect:
        result.size() == 2
    }
}
