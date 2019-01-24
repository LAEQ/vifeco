package org.laeq.model

import javafx.geometry.Point2D
import javafx.util.Duration
import spock.lang.Specification

class VideoPointTest extends Specification {
    def "returns true if the VideoPoint IsValid"(int start, int duration, int now, boolean expected) {
        given: "a video point with initial values"
        Duration s = Duration.seconds(start)
        Category category = new Category()
        Point2D point = new Point2D(10d, 10d);
        def icon = Mock(CategoryIcon)

        VideoPoint videoPoint = new VideoPoint(point, 10, duration, s, category, icon)

        when:
        Duration n = Duration.seconds(now)
        Boolean result = videoPoint.isValid(n)

        then:
        result == expected

        where:
        start | duration | now | expected
        100 | 10 | 110 | true
        100 | 10 | 111 | false
        100 | 20 | 110 | true
        100 | 20 | 110 | true
        120 | 50 | 100 | false
        120 | 50 | 120 | true
    }
}
