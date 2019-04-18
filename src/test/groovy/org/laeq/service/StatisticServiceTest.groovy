package org.laeq.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import javafx.util.Duration
import org.laeq.model.Category
import org.laeq.model.Point
import org.laeq.model.Video
import org.laeq.model.serializer.VideoSerializer
import org.laeq.model.statistic.Graph
import org.laeq.service.statistic.StatisticException
import org.laeq.service.statistic.StatisticService
import spock.lang.Specification

class StatisticServiceTest extends Specification {
    StatisticService service
    Video video1
    Video video2

    def setup(){
        service = new StatisticService()

        video1 = VideoGenerator.generateVideo(1, 10)
        video2 = VideoGenerator.generateVideo(2, 10)
    }

    def "test videos with different collections"(){
        setup:
        video2.setCollection(VideoGenerator.generateCollection(2))

        when:
        service.setVideos(video1, video2)
        service.init()

        then:
        thrown StatisticException
    }

    def "test tolerance is set" () {
        setup:
        service.setVideos(video1, video2)

        when:
        service.init()

        then:
        thrown StatisticException
    }

    def "test category map"(){
        setup:
        int pointId = 1
        service.setVideos(video1, video2)
        service.setDurationStep(Duration.seconds(10))

        when:
        VideoGenerator.generatePoints(video1, 1, 0, 10, pointId)
        pointId += 10
        VideoGenerator.generatePoints(video2, 2, 0, 10, pointId)
        pointId += 10
        VideoGenerator.generatePoints(video1, 3, 0 ,10, pointId)
        pointId += 10
        VideoGenerator.generatePoints(video2, 4, 0, 10, pointId)

        service.setDurationStep(Duration.millis(1000))
        service.init()

        then:
        service.video1CategoryMap.get(video1.collection.categorySet.find {it.id == 1}).size() == 10
        service.video2CategoryMap.get(video2.collection.categorySet.find {it.id == 2}).size() == 10
        service.video2CategoryMap.get(video2.collection.categorySet.find {it.id == 3}).size() == 0
    }

    def "test graph generation (vertices and edges) for one category"() {
        setup:
        int pointId= 1
        Video video_1 = VideoGenerator.generateVideo(1,1)
        Video video_2 = VideoGenerator.generateVideo(2, 1)

        // generate 10 points starting from 10 every seconds
        VideoGenerator.generatePoints(video_1, 1, 10, 10, pointId)
        pointId += 10
        VideoGenerator.generatePoints(video_2, 1, 10, 10, pointId)

        when:
        service.setVideos(video_1, video_2)
        service.setDurationStep(Duration.seconds(5))
        service.init()
        service.generateGraphs()

        Category category = video1.collection.categorySet.find{it.id == 1}
        Graph graph = service.getGraphByCategory(category)

        def points = new ArrayList()
        points.add(new Point(-1))
        points.addAll(video_1.pointSet)
        points.addAll(video_2.pointSet)

        then:
        graph.edges.get(graph.vertices.get(points.get(1))).size() == 6
        graph.edges.get(graph.vertices.get(points.get(2))).size()== 7
        graph.edges.get(graph.vertices.get(points.get(3))).size()== 8
        graph.edges.get(graph.vertices.get(points.get(4))).size() == 9
        graph.edges.get(graph.vertices.get(points.get(5))).size()== 10
        graph.edges.get(graph.vertices.get(points.get(6))).size() == 10
        graph.edges.get(graph.vertices.get(points.get(7))).size() == 9
        graph.edges.get(graph.vertices.get(points.get(8))).size() == 8
        graph.edges.get(graph.vertices.get(points.get(9))).size() == 7
        graph.edges.get(graph.vertices.get(points.get(10))).size() == 6
        graph.edges.get(graph.vertices.get(points.get(11))).size() == 6
        graph.edges.get(graph.vertices.get(points.get(12))).size() == 7
        graph.edges.get(graph.vertices.get(points.get(13))).size() == 8
        graph.edges.get(graph.vertices.get(points.get(14))).size() == 9
        graph.edges.get(graph.vertices.get(points.get(15))).size() == 10
        graph.edges.get(graph.vertices.get(points.get(16))).size() == 10
        graph.edges.get(graph.vertices.get(points.get(17))).size() == 9
        graph.edges.get(graph.vertices.get(points.get(18))).size() == 8
        graph.edges.get(graph.vertices.get(points.get(19))).size() == 7
        graph.edges.get(graph.vertices.get(points.get(20))).size() == 6
    }

    def "test tarjan algorithm"(){
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

    def "test execute 3 points" () {
        setup:
        Video video1 = VideoGenerator.generateVideo(1,1)
        Video video2 = VideoGenerator.generateVideo(2, 1)

        Category category = video1.collection.categorySet.find { it.id == 1}

        when:
        Point point1 = new Point(1, 10,10,Duration.millis(1000),video1, category)
        video1.pointSet.add(point1)

        Point point2 = new Point(2, 10,10,Duration.millis(1000),video2, category)
        video2.pointSet.add(point2)

        Point point3 = new Point(3, 10,10,Duration.millis(3000),video2, category)
        video2.pointSet.add(point3)

        service.setVideos(video1, video2)
        service.setDurationStep(Duration.seconds(1))
        service.execute()
        def result = service.getTarjans()

        then:
        result.get(category).collect{it.size()}.sort() == [1,2]
    }

    def "test execute"() {
        setup:
        int pointId = 1
        Video video1 = VideoGenerator.generateVideo(1,1)
        Video video2 = VideoGenerator.generateVideo(2,1)

        Category category = video1.collection.categorySet.find { it.id == 1}

        when:
        VideoGenerator.generatePoints(video1, 1, 0, 4, pointId)
        pointId += 4
        VideoGenerator.generatePoints(video2, 1, 0, 2, pointId)


        service.setVideos(video1, video2)
        service.setDurationStep(Duration.seconds(1))
        service.execute()
        def result = service.getTarjans()

        then:
        result.get(category).collect{it.collect{it.point.id}.sort()}.contains([1,2,3,5,6]) == true
        result.get(category).collect{it.collect{it.point.id}.sort()}.contains([4]) == true
    }


    def "test analyse"() {
        setup:
        int pointId = 1
        Video video1 = VideoGenerator.generateVideo(1,1)
        Video video2 = VideoGenerator.generateVideo(2,1)

        Category category = video1.collection.categorySet.find { it.id == 1}

        when:
        VideoGenerator.generatePoints(video1, 1, 0, 4, pointId) // 4 points starting at 0 every seconds
        pointId += 4
        VideoGenerator.generatePoints(video2, 1, 0, 2, pointId)  // 2 points starting at 0 every seconds


        service.setVideos(video1, video2)
        service.setDurationStep(Duration.seconds(1))
        service.execute()
        def result = service.getTarjanDiffs()

        then:
        result.get(category).values().toArray().sort() == [0, 2]
    }

    def "test analyse with 3 categories" () {
        setup:
        int pointId = 1
        Video video1 = VideoGenerator.generateVideo(1,3)
        Video video2 = VideoGenerator.generateVideo(2,3)

        Category category1 = video1.collection.categorySet.find { it.id == 1}
        Category category2 = video1.collection.categorySet.find { it.id == 2}
        Category category3 = video1.collection.categorySet.find { it.id == 3}

        when:
        VideoGenerator.generatePoints(video1, 1, 0, 4, pointId) // 4 points starting at 0 every seconds
        pointId += 4
        VideoGenerator.generatePoints(video2, 1, 0, 2, pointId)  // 2 points starting at 0 every seconds
        pointId += 2
        VideoGenerator.generatePoints(video1, 2, 0, 10, pointId)
        pointId += 10
        VideoGenerator.generatePoints(video2, 2, 4, 3, pointId)
        pointId += 3
        VideoGenerator.generatePoints(video1, 3, 20, 5, pointId)
        pointId += 5
        VideoGenerator.generatePoints(video2, 3, 23, 3 , pointId)
        pointId += 3
        VideoGenerator.generatePoints(video1, 3, 1000, 15 , pointId)
        pointId += 15
        VideoGenerator.generatePoints(video2, 3, 1007, 10 , pointId)

        service.setVideos(video1, video2)
        service.setDurationStep(Duration.seconds(1))
        service.execute()
        def result = service.getTarjanDiffs()

        then:
        result.get(category1).values().toArray().sort() == [0, 2]
        result.get(category2).values().toArray().sort() == [0,7]
        result.get(category3).values().toArray().sort() == [1,8]
    }


    def "tarjan edges for 1 second and 1 category" () {
        setup:
        int pointId = 1
        Video video1 = VideoGenerator.generateVideo(1,3)
        Video video2 = VideoGenerator.generateVideo(2,3)

        Category category1 = video1.collection.categorySet.find { it.id == 1}
        Category category2 = video1.collection.categorySet.find { it.id == 2}
        Category category3 = video1.collection.categorySet.find { it.id == 3}

        when:
        VideoGenerator.generatePoints(video1, 1, 0, 4, pointId) // 4 points starting at 0 every seconds
        pointId += 4
        VideoGenerator.generatePoints(video2, 1, 0, 2, pointId)  // 2 points starting at 0 every seconds

        service.setVideos(video1, video2)
        service.setDurationStep(Duration.seconds(1))
        service.execute()

        def edges = service.getTarjanEdges()

        then:
        edges.find {it.key.id == 1}.value.collect{[it.start.point.id, it.end.point.id]} == [[6,2], [5,1]]
    }

    def "tarjan edges for 2 second and 1 category" () {
        setup:
        int pointId = 1
        Video video1 = VideoGenerator.generateVideo(1,3)
        Video video2 = VideoGenerator.generateVideo(2,3)

        when:
        VideoGenerator.generatePoints(video1, 1, 0, 4, pointId) // 4 points starting at 0 every seconds
        pointId += 4
        VideoGenerator.generatePoints(video2, 1, 0, 2, pointId)  // 2 points starting at 0 every seconds
        pointId += 2
        VideoGenerator.generatePoints(video1, 1, 10, 10, pointId)
        pointId += 10
        VideoGenerator.generatePoints(video2, 1, 14, 3, pointId)
        pointId += 3
        VideoGenerator.generatePoints(video1, 1, 20, 5, pointId)
        pointId += 5
        VideoGenerator.generatePoints(video2, 1, 23, 3 , pointId)
        pointId += 3
        VideoGenerator.generatePoints(video1, 1, 1000, 15 , pointId)
        pointId += 15
        VideoGenerator.generatePoints(video2, 1, 1007, 10 , pointId)
        pointId += 10

        service.setVideos(video1, video2)
        service.setDurationStep(Duration.seconds(1))
        service.execute()

        def edges = service.getTarjanEdges()
        def result = edges.find {it.key.id == 1}.value.collect{[it.start.point.id, it.end.point.id]}.sort {it[0]}

        then:
        result == [[5,1], [6,2], [17,11], [18,12], [19,13], [22,25], [23,26], [24,27], [34,43], [35,44], [36,45], [37,46], [38,47], [39,48], [40,49], [41,50], [42,51]]
    }

    def "tarjan edges for 5 second and 1 category" () {
        setup:
        int pointId = 1
        Video video1 = VideoGenerator.generateVideo(1,1)
        Video video2 = VideoGenerator.generateVideo(2,1)

        when:
        VideoGenerator.generatePoints(video1, 1, 0, 4, pointId) // 4 points starting at 0 every seconds
        pointId += 4
        VideoGenerator.generatePoints(video2, 1, 7, 2, pointId)  // 2 points starting at 0 every seconds
        pointId += 2
        VideoGenerator.generatePoints(video1, 1, 10, 16, pointId)
        pointId += 16
        VideoGenerator.generatePoints(video2, 1, 18, 3, pointId)

        service.setVideos(video1, video2)
        service.setDurationStep(Duration.seconds(3))
        service.execute()

        def edges = service.getTarjanEdges()
        def lonelyPoints = service.getLonelyPoints()
        def result = edges.find {it.key.id == 1}.value.collect{[it.start.point.id, it.end.point.id]}.sort {it[0]}



        then:
        result == [[7,5], [8,6], [23,15], [24,16], [25,17]]
        lonelyPoints.find{ it.key.id == 1}.value.collect {it.point.id }.sort()  == [1,2,3,4,9,10,11,12,13,14,18,19,20,21,22]
    }

    def "serialize tarjanDiff" () {
        setup:
        int pointId = 1
        Video video1 = VideoGenerator.generateVideo(1, 3)
        Video video2 = VideoGenerator.generateVideo(2, 3)

        when:
        VideoGenerator.generatePoints(video1, 1, 0, 4, pointId) // 4 points starting at 0 every seconds
        pointId += 4
        VideoGenerator.generatePoints(video2, 1, 0, 2, pointId)  // 2 points starting at 0 every seconds
        pointId += 2
        VideoGenerator.generatePoints(video1, 2, 0, 10, pointId)
        pointId += 10
        VideoGenerator.generatePoints(video2, 2, 4, 3, pointId)
        pointId += 3
        VideoGenerator.generatePoints(video1, 3, 20, 5, pointId)
        pointId += 5
        VideoGenerator.generatePoints(video2, 3, 23, 3, pointId)
        pointId += 3
        VideoGenerator.generatePoints(video1, 3, 1000, 15, pointId)
        pointId += 15
        VideoGenerator.generatePoints(video2, 3, 1007, 10, pointId)

        service.setVideos(video1, video2)
        service.setDurationStep(Duration.seconds(1))
        service.execute()

        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(Video.class, new VideoSerializer())
        mapper.registerModule(module)

        String result = mapper.writeValueAsString(service)

        then:
        true == true
    }
}
