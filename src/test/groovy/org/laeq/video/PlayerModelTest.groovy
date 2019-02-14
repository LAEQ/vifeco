package org.laeq.video

import javafx.util.Duration
import org.laeq.model.Category
import org.laeq.model.CategoryIcon
import org.laeq.model.Point
import org.laeq.model.VideoPoint
import spock.lang.Specification

class PlayerModelTest extends Specification {
    PlayerModel model
    int id
    void setup() {
        model = new PlayerModel();
        id = 0
    }

    int getNextid(){
        id++
        return id
    }

    def "test add 1000 points"() {
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
        def result = model.getVideoPoints()

        then:
        result.size() == 20000
    }

    def "test add 10 points and check the ordered"() {
        setup:
        def id = 0;
        3.downto(0, {
            Point point = generatePoint(it)
            point.id = getNextid()
            model.addPoint(point)
        })

        2.upto(5, {
            Point point = generatePoint(it)
            point.id = getNextid()
            model.addPoint(point)
        })

        when:
        def result = model.getVideoPoints()
        println result

        then:
        result.size() == getNextid() - 1
        result.collect{ it.point.start.toMillis()} == [0, 1, 2, 2, 3, 3, 4, 5]
        result.collect{ it.point.id} == [4, 3, 2, 5, 1, 6, 7, 8]
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
        Iterator<VideoPoint> iter = model.displayIter(Duration.millis(now))

        then:
        iter.size() == expected

        where:
        total | duration | now | expected
        1000  | 10       | 0     | 1001
        1000  | 1        | 0     | 1000
        10000 | 3        | 3     | 3000
        10000 | 3        | 700   | 3000
        10000 | 3        | 10000 | 1
        10000 | 3        | 10001 | 0
        10000 | 3.77     | 899   | 3000
    }

    def "test list of points to display: ids"(int total, int duration, int now, int firstId, int lastId) {
        setup:
        0.upto(total, {
            Point point = generatePoint(it)
            point.id = getNextid()
            model.addPoint(point)
        })

        model.setDuration(duration)

        when:
        Iterator<VideoPoint> iter = model.displayIter(Duration.millis(now))

        then:
        iter.next().point.id == firstId

        int last = firstId
        while(iter.hasNext()){
            last = iter.next().point.id
        }

        last == lastId

        where:
        total | duration | now | firstId | lastId
        1000  | 10       | 0     | 1     | 1001
        1000  | 1        | 0     | 1     | 1000
        10000 | 3        | 3     | 4     | 3003
        10000 | 3        | 700   | 701   | 3700
        10000 | 3        | 10000 | 10001 | 10001
        10000 | 3.77     | 899   | 900   | 3899
    }

    Point generatePoint(int start){
        Point point = new Point(0, 0, Duration.millis(start), null, null, null)
        return point
    }
}
