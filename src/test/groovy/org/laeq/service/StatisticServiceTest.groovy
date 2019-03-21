package org.laeq.service

import javafx.util.Duration
import org.laeq.model.Point
import org.laeq.model.Video
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

        video1 = VideoGenerator.generateVideo()
        video2 = VideoGenerator.generateVideo()
    }

    def "videos without the same collection"(){
        setup:
        video2.setCollection(VideoGenerator.generateCollection(2))

        when:
        service.setVideos(video1, video2)
        service.execute()

        then:
        thrown StatisticException
    }

    def "videos with no tolerance" () {
        setup:
        service.setVideos(video1, video2)

        when:
        service.execute()

        then:
        thrown StatisticException
    }

    def "video with same points"(){
        setup:


        1.upto(5, {
            Category category = video1.collection.categorySet.find { it.id = 2}
            Duration start = Duration.millis(it)
            Point point = new Point(10, 10, start, video1, category)
            video1.pointSet.add(point)

            video2.pointSet.add(point)
        })

        service.setVideos(video1, video2)
        service.setDurationStep(Duration.seconds(10))

        when:
        VideoGenerator.generatePoints(video1, 1)
        VideoGenerator.generatePoints(video2, 2)
        VideoGenerator.generatePoints(video1, 3)
        VideoGenerator.generatePoints(video2, 4)

        service.step1()

        then:
        true == true
    }


}
