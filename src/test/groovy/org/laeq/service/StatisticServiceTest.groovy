package org.laeq.service


import javafx.util.Duration
import org.laeq.StatisticService
import org.laeq.model.Category
import org.laeq.model.Point
import org.laeq.model.Video
import org.laeq.model.dao.EntityGenerator
import org.laeq.model.statistic.Graph
import spock.lang.Specification

class StatisticServiceTest extends Specification {
    StatisticService service
    Video video1
    Video video2

    def setup(){
        service = new StatisticService()

        video1 = VideoGenerator.generateVideo(10)
        video2 = VideoGenerator.generateVideo(10)
    }

    def "test category map"(){
        setup:
        VideoGenerator.generatePoints(video1, 1, 0, 10)
        VideoGenerator.generatePoints(video2, 1, 0, 5)
        VideoGenerator.generatePoints(video1, 2, 0 ,10)
        VideoGenerator.generatePoints(video2, 3, 0, 10)

        when:
        service.execute(Arrays.asList(video1, video2), 1)

        then:
        def expected = [10,10,0,0,0,0,0,0,0,0]
        def expected2 = [5,0,10,0,0,0,0,0,0,0]
        for (i in 1..10) {
            service.video1CategoryMap.get(video1.collection.categories.find {it.id == i}).size() == expected[i]
            service.video2CategoryMap.get(video2.collection.categories.find {it.id == i}).size() == expected2[i]
        }
    }

    def "test graph generation (vertices and edges) for one category"() {
        setup:
        Video video_1 = VideoGenerator.generateVideo(2)
        Video video_2 = VideoGenerator.generateVideo(2)

        // generate 10 points starting from 10 every seconds
        VideoGenerator.generatePoints(video_1, 1, 1, 10)
        VideoGenerator.generatePoints(video_2, 1, 1, 10)

        when:
        service.execute([video_1, video_2], 5)

        Category category = video1.collection.categories.find{it.id == 1}
        Graph graph = service.getGraphByCategory(category)

        def points = []
        points.addAll(video_1.points)
        points.addAll(video_2.points)

        then:
        def expected = [6,7,8,9,10,10,9,8,7,6,6,7,8,9,10,10,9,8,7,6]
        graph.vertices.size() == 20
        0.upto(19, {
            graph.edges.get(graph.vertices.get(points.get(it))).size()== expected[it]
        })
    }

    def "test tarjan algorithm"(){
        setup:
        Graph graph = new Graph()

        Point a = new Point(UUID.randomUUID())
        Point b = new Point(UUID.randomUUID())
        Point c = new Point(UUID.randomUUID())
        Point d = new Point(UUID.randomUUID())
        Point e = new Point(UUID.randomUUID())
        Point f = new Point(UUID.randomUUID())
        Point g = new Point(UUID.randomUUID())
        Point h = new Point(UUID.randomUUID())

        def points = [a,b,c,d,e,f,g,h]
        Category category = EntityGenerator.createCategory('A')
        points.eachWithIndex{it, index ->
            it.category = category
            it.start = Duration.seconds(1 * (index + 1))
        }

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
        def resultIds = resultList.collect {it.collect {it.point}.sort()}

        then:
        resultIds.contains([h]) == true
        resultIds.contains([f]) == true
        resultIds.contains([d,e,g]) == true
        resultIds.contains([a,b,c]) == true
    }

    def "test execute 3 points" () {
        setup:
        Video video1 = VideoGenerator.generateVideo(1)
        Video video2 = VideoGenerator.generateVideo(1)
        video2.collection = video1.collection

        Category category = video1.collection.categories.find { it.id == 1}

        when:
        Point point1 = new Point(UUID.randomUUID(), 10,10,Duration.millis(1000),category, video1)
        video1.points.add(point1)

        Point point2 = new Point(UUID.randomUUID(), 10,10,Duration.millis(1000), category, video2)
        video2.points.add(point2)

        Point point3 = new Point(UUID.randomUUID(), 10,10,Duration.millis(3000),category, video2)
        video2.points.add(point3)

        service.execute([video1, video2], 1)
        def result = service.getTarjans()

        then:
        result.get(category).collect{it.size()}.sort() == [1,2]
    }

    def "test execute"() {
        setup:
        Video video1 = VideoGenerator.generateVideo(1)
        Video video2 = VideoGenerator.generateVideo(1)

        Category category = video1.collection.categories.find { it.id == 1}

        when:
        VideoGenerator.generatePoints(video1, 1, 1, 4)
        VideoGenerator.generatePoints(video2, 1, 1, 2)

        service.execute([video1, video2], 1)
        def result = service.getTarjans()

        then:
        def expecte1 = video1.points.take(3) + video2.points
        def expecte2 = [video1.points.last()]
        result.get(category).collect{it.collect{it.point.id}.sort()}.contains(expecte1.collect{it.id}.sort()) == true
        result.get(category).collect{it.collect{it.point.id}.sort()}.contains(expecte2.collect{it.id}.sort()) == true
    }


    def "test analyse"() {
        setup:
        Video video1 = VideoGenerator.generateVideo(1)
        Video video2 = VideoGenerator.generateVideo(1)

        Category category = video1.collection.categories.find { it.id == 1}

        when:
        VideoGenerator.generatePoints(video1, 1, 1, 4)
        VideoGenerator.generatePoints(video2, 1, 1, 2)
        VideoGenerator.generatePoints(video1, 1, 10, 4)
        VideoGenerator.generatePoints(video2, 1, 11, 2)
        VideoGenerator.generatePoints(video1, 1, 100, 1)
        VideoGenerator.generatePoints(video2, 1, 101, 2)


        service.execute([video1, video2], 1)
        def result = service.getTarjanDiff()
        def matchedPoints = result[0].getMatchedPoints();
        def summary = result[0].getSummary();

        then:
        result.size() == 1
        summary.get(video1).matched == 4
        summary.get(video1).lonely == 5
        summary.get(video2).matched == 4
        summary.get(video2).lonely == 2
        matchedPoints.size() == 11
    }

    def "test analyse with 3 categories" () {
        setup:
        Video video1 = VideoGenerator.generateVideo(3)
        Video video2 = VideoGenerator.generateVideo(3)

        Category category1 = video1.collection.categories.find { it.id == 1}
        Category category2 = video1.collection.categories.find { it.id == 2}
        Category category3 = video1.collection.categories.find { it.id == 3}

        when:
        VideoGenerator.generatePoints(video1, 1, 1, 4) // 4 points starting at 0 every seconds
        VideoGenerator.generatePoints(video2, 1, 1, 2)  // 2 points starting at 0 every seconds
        VideoGenerator.generatePoints(video1, 2, 1, 10)
        VideoGenerator.generatePoints(video2, 2, 4, 3)
        VideoGenerator.generatePoints(video1, 3, 20, 5)
        VideoGenerator.generatePoints(video2, 3, 23, 3)

        service.execute([video1, video2], 1)
        def result = service.getTarjanDiff()
        def summary1 = result.get(0)
        def summary2 = result.get(1)
        def summary3 = result.get(2)


        then:
        result.size() == 3
        summary1.summary.get(video1).lonely == 2
        summary1.summary.get(video2).lonely == 0
        summary2.summary.get(video1).lonely == 8
        summary2.summary.get(video2).lonely == 1
        summary3.summary.get(video1).lonely == 5
        summary3.summary.get(video2).lonely == 3
    }
}
