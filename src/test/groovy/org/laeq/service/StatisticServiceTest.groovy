package org.laeq.service

import javafx.util.Duration
import org.laeq.model.statistic.Graph
import org.laeq.model.Point
import org.laeq.model.User
import org.laeq.model.Vertex
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
        graph.vertices.get(new Vertex(new Point(1, Duration.millis(0)))).size() == 6
        graph.vertices.get(new Vertex(new Point(2, Duration.millis(0)))).size() == 7
        graph.vertices.get(new Vertex(new Point(3, Duration.millis(0)))).size() == 8
        graph.vertices.get(new Vertex(new Point(4, Duration.millis(0)))).size() == 9
        graph.vertices.get(new Vertex(new Point(5, Duration.millis(0)))).size() == 10
        graph.vertices.get(new Vertex(new Point(6, Duration.millis(0)))).size() == 10
        graph.vertices.get(new Vertex(new Point(7, Duration.millis(0)))).size() == 9
        graph.vertices.get(new Vertex(new Point(8, Duration.millis(0)))).size() == 8
        graph.vertices.get(new Vertex(new Point(9, Duration.millis(0)))).size() == 7
        graph.vertices.get(new Vertex(new Point(10, Duration.millis(0)))).size() == 6
        graph.vertices.get(new Vertex(new Point(11, Duration.millis(0)))).size() == 6
        graph.vertices.get(new Vertex(new Point(12, Duration.millis(0)))).size() == 7
        graph.vertices.get(new Vertex(new Point(13, Duration.millis(0)))).size() == 8
        graph.vertices.get(new Vertex(new Point(14, Duration.millis(0)))).size() == 9
        graph.vertices.get(new Vertex(new Point(15, Duration.millis(0)))).size() == 10
        graph.vertices.get(new Vertex(new Point(16, Duration.millis(0)))).size() == 10
        graph.vertices.get(new Vertex(new Point(17, Duration.millis(0)))).size() == 9
        graph.vertices.get(new Vertex(new Point(18, Duration.millis(0)))).size() == 8
        graph.vertices.get(new Vertex(new Point(19, Duration.millis(0)))).size() == 7
        graph.vertices.get(new Vertex(new Point(20, Duration.millis(0)))).size() == 6
    }
}
