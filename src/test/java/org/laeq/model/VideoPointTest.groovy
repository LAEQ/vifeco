package org.laeq.model

import javafx.util.Duration
import spock.lang.Specification

class VideoPointTest extends Specification {
    def "returns true if the VideoPoint IsValid"(int a, int b, int c, boolean d) {
        given: "a video point with initial values"
        Duration start = Duration.seconds(a);
        Category category = new Category();
        VideoPoint videoPoint = new VideoPoint(10d, 10d, 10, b, start, category)

        when:
        Duration now = Duration.seconds(c);
        Boolean result = videoPoint.isValid(now);

        then:
        result == d

        where:
        a | b | c | d
        100 | 10 | 110 | true
        100 | 10 | 111 | false
        100 | 20 | 110 | true
    }
}
