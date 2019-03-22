package org.laeq.service

import javafx.util.Duration
import org.laeq.model.statistic.Graph
import org.laeq.model.Point
import org.laeq.model.Video
import org.laeq.model.statistic.Vertex
import org.laeq.service.statistic.StatisticException
import org.laeq.service.statistic.StatisticService
import spock.lang.Specification
import org.laeq.model.Category


class StatisticServiceTest extends Specification {
    StatisticService service
    Video video1
    Video video2

    def setup(){
        service = new StatisticService()

        video1 = VideoGenerator.generateVideo(10)
        video2 = VideoGenerator.generateVideo(10)
    }

    def "test videos with different collections"(){
        setup:
        video2.setCollection(VideoGenerator.generateCollection(2))

        when:
        service.setVideos(video1, video2)
        service.execute()

        then:
        thrown StatisticException
    }

    def "test tolerance is set" () {
        setup:
        service.setVideos(video1, video2)

        when:
        service.execute()

        then:
        thrown StatisticException
    }

    def "test category map"(){
        setup:
        service.setVideos(video1, video2)
        service.setDurationStep(Duration.seconds(10))

        when:
        VideoGenerator.generatePoints(video1, 1, 0)
        VideoGenerator.generatePoints(video2, 2, 0)
        VideoGenerator.generatePoints(video1, 3, 0 )
        VideoGenerator.generatePoints(video2, 4, 0)

        service.setDurationStep(Duration.millis(1000))
        service.execute()

        then:
        service.video1CategoryMap.get(video1.collection.categorySet.find {it.id == 1}).size() == 10
        service.video2CategoryMap.get(video2.collection.categorySet.find {it.id == 2}).size() == 10
        service.video2CategoryMap.get(video2.collection.categorySet.find {it.id == 3}).size() == 0
    }

    def "test graph generation for a category"() {
        setup:
        Video video_1 = VideoGenerator.generateVideo(1)
        Video video_2 = VideoGenerator.generateVideo(1)

        // generate 10 points starting from 10 every seconds
        VideoGenerator.generatePoints(video_1, 1, 10)
        VideoGenerator.generatePoints(video_2, 1, 10)

        when:
        service.setVideos(video_1, video_2)
        service.setDurationStep(Duration.seconds(5))
        service.execute()
        service.generateGraphs()

        Category category = video1.collection.categorySet.find{it.id == 1}
        Graph graph = service.getGraphByCategory(category)

        then:
        graph.edges.get(new Vertex(new Point(1, Duration.millis(0)))).size() == 6
        graph.edges.get(new Vertex(new Point(2, Duration.millis(0)))).size() == 7
        graph.edges.get(new Vertex(new Point(3, Duration.millis(0)))).size() == 8
        graph.edges.get(new Vertex(new Point(4, Duration.millis(0)))).size() == 9
        graph.edges.get(new Vertex(new Point(5, Duration.millis(0)))).size() == 10
        graph.edges.get(new Vertex(new Point(6, Duration.millis(0)))).size() == 10
        graph.edges.get(new Vertex(new Point(7, Duration.millis(0)))).size() == 9
        graph.edges.get(new Vertex(new Point(8, Duration.millis(0)))).size() == 8
        graph.edges.get(new Vertex(new Point(9, Duration.millis(0)))).size() == 7
        graph.edges.get(new Vertex(new Point(10, Duration.millis(0)))).size() == 6
        graph.edges.get(new Vertex(new Point(11, Duration.millis(0)))).size() == 6
        graph.edges.get(new Vertex(new Point(12, Duration.millis(0)))).size() == 7
        graph.edges.get(new Vertex(new Point(13, Duration.millis(0)))).size() == 8
        graph.edges.get(new Vertex(new Point(14, Duration.millis(0)))).size() == 9
        graph.edges.get(new Vertex(new Point(15, Duration.millis(0)))).size() == 10
        graph.edges.get(new Vertex(new Point(16, Duration.millis(0)))).size() == 10
        graph.edges.get(new Vertex(new Point(17, Duration.millis(0)))).size() == 9
        graph.edges.get(new Vertex(new Point(18, Duration.millis(0)))).size() == 8
        graph.edges.get(new Vertex(new Point(19, Duration.millis(0)))).size() == 7
        graph.edges.get(new Vertex(new Point(20, Duration.millis(0)))).size() == 6
    }

    def "test tarjan 1"(){
        setup:
        Graph graph = new Graph()
        Point a = new Point(1)
        Point b = new Point(2)
        Point c = new Point(3)
        Point d = new Point(4)
        Point e = new Point(5)
        Point f = new Point(6)
        Point g = new Point(7)
        Point h = new Point(8)

        graph.addVertex(a)
        graph.addVertex(b)
        graph.addVertex(c)
        graph.addVertex(d)
        graph.addVertex(e)
        graph.addVertex(f)
        graph.addVertex(g)
        graph.addVertex(h)

        
        graph.addEdges(a, b)
        graph.addEdges(b, a)
        graph.addEdges(b, c)
        graph.addEdges(c, b)
        graph.addEdges(c, a)

        graph.addEdges(a, e)

        graph.addEdges(b, f)

        graph.addEdges(f, d)

        graph.addEdges(d, e)
        graph.addEdges(d, h)
        graph.addEdges(e, d)
        graph.addEdges(e, g)
        graph.addEdges(g, e)

        graph.addEdges(g, h)

        when:
        def resultList = graph.tarjan()
        def resultIds = resultList.collect {it.collect {it.point.id}.sort()}
        println resultIds


        then:
        resultIds.contains([8]) == true
        resultIds.contains([6]) == true
        resultIds.contains([4,5,7]) == true
        resultIds.contains([1,2,3]) == true
    }


}
