package org.laeq.video

import javafx.util.Duration
import org.laeq.model.*
import org.laeq.video.player.PlayerModel
import spock.lang.Specification

class PlayerModelTest extends Specification {
    PlayerModel model
    Video video

    int id
    void setup() {
        model = new PlayerModel()
        User user = new User(1, "test", "test", "test")
        CategoryCollection cc = new CategoryCollection(1, "Default", false)
        video = new Video(1, "test", Duration.millis(100000), user, cc)

        model.setVideo(video)
        model.setDuration(10000)
        id = 0
    }

    int getNextid(){
        id++
        return id
    }

    def "test get icons to display"() {
        setup:
        def id = 0
        1.upto(10000, {
            Point point = generatePoint(it)
            point.id = getNextid()
            model.addPoint(point)
        })

        1.upto(10000, {
            Point point = generatePoint(it)
            point.id = getNextid()
            model.addPoint(point)
        })

        when:
        def result = model.displayPoints(Duration.millis(0))

        then:
        result.size() == 0

    }


    def "test list of points to display: size"(int total, int duration, int now, int expected) {
        setup:
        0.upto(total, {
            Point point = generatePoint(it)
            point.id = getNextid()
            model.addPoint(point)
        })

        model.setDuration(duration)

        when:
        Collection<Point> result = model.displayPoints(Duration.millis(now))

        println result

        then:
        result.size() == expected

        where:
        total | duration | now      | expected
        1000  | 10       | 0        | 0
        1000  | 10       | 1000     | 1000
        10000 | 1        | 3        | 3
//        10000 | 2        | 700      | 2701
//        10000 | 3        | 10000    | 3
//        10000 | 3        | 10003    | 1
//        10000 | 3.77     | 899      | 3
    }


    Point generatePoint(int start){

        Category category = new Category("test", "M150 0 L75 200 L225 200 Z", "test", "A")
        Point point = new Point(getNextid(), 0, 0, Duration.millis(start), video, category)

        return point
    }
}
