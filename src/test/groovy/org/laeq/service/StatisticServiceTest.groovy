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

        then:
        graph.edges.get(graph.vertices.get(new Point(1))).size() == 6
        graph.edges.get(graph.vertices.get(new Point(2))).size()== 7
        graph.edges.get(graph.vertices.get(new Point(3))).size()== 8
        graph.edges.get(graph.vertices.get(new Point(4))).size() == 9
        graph.edges.get(graph.vertices.get(new Point(5))).size()== 10
        graph.edges.get(graph.vertices.get(new Point(6))).size() == 10
        graph.edges.get(graph.vertices.get(new Point(7))).size() == 9
        graph.edges.get(graph.vertices.get(new Point(8))).size() == 8
        graph.edges.get(graph.vertices.get(new Point(9))).size() == 7
        graph.edges.get(graph.vertices.get(new Point(10))).size() == 6
        graph.edges.get(graph.vertices.get(new Point(11))).size() == 6
        graph.edges.get(graph.vertices.get(new Point(12))).size() == 7
        graph.edges.get(graph.vertices.get(new Point(13))).size() == 8
        graph.edges.get(graph.vertices.get(new Point(14))).size() == 9
        graph.edges.get(graph.vertices.get(new Point(15))).size() == 10
        graph.edges.get(graph.vertices.get(new Point(16))).size() == 10
        graph.edges.get(graph.vertices.get(new Point(17))).size() == 9
        graph.edges.get(graph.vertices.get(new Point(18))).size() == 8
        graph.edges.get(graph.vertices.get(new Point(19))).size() == 7
        graph.edges.get(graph.vertices.get(new Point(20))).size() == 6
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
        result.get(category).values().toArray() == [2, 0]
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
        result.get(category1).values().toArray() == [2,0]
        result.get(category2).values().toArray() == [7,0]
        result.get(category3).values().toArray() == [8,1]
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
        Video video1 = VideoGenerator.generateVideo(1,3)
        Video video2 = VideoGenerator.generateVideo(2,3)

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

        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(Video.class, new VideoSerializer())
        mapper.registerModule(module)

        String result = mapper.writeValueAsString(service)

        then:
        result == '{"videos":{"video_1":{"name":"path","duration":10000.0,"user":{"id":1,"firstName":"test","lastName":"test","email":"test@test.com","isDefault":false},"collection":{"id":1,"name":"test","isDefault":false,"categorySet":[{"id":1,"name":"cat_1","icon":null,"color":null,"shortcut":null},{"id":2,"name":"cat_2","icon":null,"color":null,"shortcut":null},{"id":3,"name":"cat_3","icon":null,"color":null,"shortcut":null}]}},"video_2":{"name":"path","duration":10000.0,"user":{"id":1,"firstName":"test","lastName":"test","email":"test@test.com","isDefault":false},"collection":{"id":1,"name":"test","isDefault":false,"categorySet":[{"id":1,"name":"cat_1","icon":null,"color":null,"shortcut":null},{"id":2,"name":"cat_2","icon":null,"color":null,"shortcut":null},{"id":3,"name":"cat_3","icon":null,"color":null,"shortcut":null}]}}},"tarjan_diff":{"Cat{1 cat_1}":{"Video{1}":2,"Video{2}":0},"Cat{2 cat_2}":{"Video{1}":7,"Video{2}":0},"Cat{3 cat_3}":{"Video{1}":8,"Video{2}":1}},"tarjan_edge":{"Cat{1 cat_1}":[{"start":{"point":{"id":6,"x":10.0,"y":10.0,"categoryId":1,"startDouble":2000.0,"videoId":2}},"end":{"point":{"id":2,"x":10.0,"y":10.0,"categoryId":1,"startDouble":2000.0,"videoId":1}},"deltaStart":0.0},{"start":{"point":{"id":5,"x":10.0,"y":10.0,"categoryId":1,"startDouble":1000.0,"videoId":2}},"end":{"point":{"id":1,"x":10.0,"y":10.0,"categoryId":1,"startDouble":1000.0,"videoId":1}},"deltaStart":0.0}],"Cat{2 cat_2}":[{"start":{"point":{"id":19,"x":10.0,"y":10.0,"categoryId":2,"startDouble":7000.0,"videoId":2}},"end":{"point":{"id":13,"x":10.0,"y":10.0,"categoryId":2,"startDouble":7000.0,"videoId":1}},"deltaStart":0.0},{"start":{"point":{"id":18,"x":10.0,"y":10.0,"categoryId":2,"startDouble":6000.0,"videoId":2}},"end":{"point":{"id":12,"x":10.0,"y":10.0,"categoryId":2,"startDouble":6000.0,"videoId":1}},"deltaStart":0.0},{"start":{"point":{"id":17,"x":10.0,"y":10.0,"categoryId":2,"startDouble":5000.0,"videoId":2}},"end":{"point":{"id":11,"x":10.0,"y":10.0,"categoryId":2,"startDouble":5000.0,"videoId":1}},"deltaStart":0.0}],"Cat{3 cat_3}":[{"start":{"point":{"id":24,"x":10.0,"y":10.0,"categoryId":3,"startDouble":25000.0,"videoId":1}},"end":{"point":{"id":27,"x":10.0,"y":10.0,"categoryId":3,"startDouble":26000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":23,"x":10.0,"y":10.0,"categoryId":3,"startDouble":24000.0,"videoId":1}},"end":{"point":{"id":26,"x":10.0,"y":10.0,"categoryId":3,"startDouble":25000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":22,"x":10.0,"y":10.0,"categoryId":3,"startDouble":23000.0,"videoId":1}},"end":{"point":{"id":25,"x":10.0,"y":10.0,"categoryId":3,"startDouble":24000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":42,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1015000.0,"videoId":1}},"end":{"point":{"id":51,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1016000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":41,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1014000.0,"videoId":1}},"end":{"point":{"id":50,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1015000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":40,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1013000.0,"videoId":1}},"end":{"point":{"id":49,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1014000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":39,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1012000.0,"videoId":1}},"end":{"point":{"id":48,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1013000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":38,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1011000.0,"videoId":1}},"end":{"point":{"id":47,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1012000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":37,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1010000.0,"videoId":1}},"end":{"point":{"id":46,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1011000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":36,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1009000.0,"videoId":1}},"end":{"point":{"id":45,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1010000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":35,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1008000.0,"videoId":1}},"end":{"point":{"id":44,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1009000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":34,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1007000.0,"videoId":1}},"end":{"point":{"id":43,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1008000.0,"videoId":2}},"deltaStart":1000.0}]},"tarjan_edge":{"Cat{1 cat_1}":[{"start":{"point":{"id":6,"x":10.0,"y":10.0,"categoryId":1,"startDouble":2000.0,"videoId":2}},"end":{"point":{"id":2,"x":10.0,"y":10.0,"categoryId":1,"startDouble":2000.0,"videoId":1}},"deltaStart":0.0},{"start":{"point":{"id":5,"x":10.0,"y":10.0,"categoryId":1,"startDouble":1000.0,"videoId":2}},"end":{"point":{"id":1,"x":10.0,"y":10.0,"categoryId":1,"startDouble":1000.0,"videoId":1}},"deltaStart":0.0}],"Cat{2 cat_2}":[{"start":{"point":{"id":19,"x":10.0,"y":10.0,"categoryId":2,"startDouble":7000.0,"videoId":2}},"end":{"point":{"id":13,"x":10.0,"y":10.0,"categoryId":2,"startDouble":7000.0,"videoId":1}},"deltaStart":0.0},{"start":{"point":{"id":18,"x":10.0,"y":10.0,"categoryId":2,"startDouble":6000.0,"videoId":2}},"end":{"point":{"id":12,"x":10.0,"y":10.0,"categoryId":2,"startDouble":6000.0,"videoId":1}},"deltaStart":0.0},{"start":{"point":{"id":17,"x":10.0,"y":10.0,"categoryId":2,"startDouble":5000.0,"videoId":2}},"end":{"point":{"id":11,"x":10.0,"y":10.0,"categoryId":2,"startDouble":5000.0,"videoId":1}},"deltaStart":0.0}],"Cat{3 cat_3}":[{"start":{"point":{"id":24,"x":10.0,"y":10.0,"categoryId":3,"startDouble":25000.0,"videoId":1}},"end":{"point":{"id":27,"x":10.0,"y":10.0,"categoryId":3,"startDouble":26000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":23,"x":10.0,"y":10.0,"categoryId":3,"startDouble":24000.0,"videoId":1}},"end":{"point":{"id":26,"x":10.0,"y":10.0,"categoryId":3,"startDouble":25000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":22,"x":10.0,"y":10.0,"categoryId":3,"startDouble":23000.0,"videoId":1}},"end":{"point":{"id":25,"x":10.0,"y":10.0,"categoryId":3,"startDouble":24000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":42,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1015000.0,"videoId":1}},"end":{"point":{"id":51,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1016000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":41,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1014000.0,"videoId":1}},"end":{"point":{"id":50,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1015000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":40,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1013000.0,"videoId":1}},"end":{"point":{"id":49,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1014000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":39,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1012000.0,"videoId":1}},"end":{"point":{"id":48,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1013000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":38,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1011000.0,"videoId":1}},"end":{"point":{"id":47,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1012000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":37,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1010000.0,"videoId":1}},"end":{"point":{"id":46,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1011000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":36,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1009000.0,"videoId":1}},"end":{"point":{"id":45,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1010000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":35,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1008000.0,"videoId":1}},"end":{"point":{"id":44,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1009000.0,"videoId":2}},"deltaStart":1000.0},{"start":{"point":{"id":34,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1007000.0,"videoId":1}},"end":{"point":{"id":43,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1008000.0,"videoId":2}},"deltaStart":1000.0}]},"lonely_points":{"Cat{1 cat_1}":[{"point":{"id":3,"x":10.0,"y":10.0,"categoryId":1,"startDouble":3000.0,"videoId":1}},{"point":{"id":4,"x":10.0,"y":10.0,"categoryId":1,"startDouble":4000.0,"videoId":1}}],"Cat{2 cat_2}":[{"point":{"id":7,"x":10.0,"y":10.0,"categoryId":2,"startDouble":1000.0,"videoId":1}},{"point":{"id":8,"x":10.0,"y":10.0,"categoryId":2,"startDouble":2000.0,"videoId":1}},{"point":{"id":9,"x":10.0,"y":10.0,"categoryId":2,"startDouble":3000.0,"videoId":1}},{"point":{"id":10,"x":10.0,"y":10.0,"categoryId":2,"startDouble":4000.0,"videoId":1}},{"point":{"id":14,"x":10.0,"y":10.0,"categoryId":2,"startDouble":8000.0,"videoId":1}},{"point":{"id":15,"x":10.0,"y":10.0,"categoryId":2,"startDouble":9000.0,"videoId":1}},{"point":{"id":16,"x":10.0,"y":10.0,"categoryId":2,"startDouble":10000.0,"videoId":1}}],"Cat{3 cat_3}":[{"point":{"id":33,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1006000.0,"videoId":1}},{"point":{"id":52,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1017000.0,"videoId":2}},{"point":{"id":20,"x":10.0,"y":10.0,"categoryId":3,"startDouble":21000.0,"videoId":1}},{"point":{"id":21,"x":10.0,"y":10.0,"categoryId":3,"startDouble":22000.0,"videoId":1}},{"point":{"id":28,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1001000.0,"videoId":1}},{"point":{"id":29,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1002000.0,"videoId":1}},{"point":{"id":30,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1003000.0,"videoId":1}},{"point":{"id":31,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1004000.0,"videoId":1}},{"point":{"id":32,"x":10.0,"y":10.0,"categoryId":3,"startDouble":1005000.0,"videoId":1}}]}}'
//        result.get(category1).values().toArray() == [2,0]
//        result.get(category2).values().toArray() == [7,0]
//        result.get(category3).values().toArray() == [8,1]
    }
}
