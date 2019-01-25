package org.laeq.video

import javafx.util.Duration
import spock.lang.Specification

import static spock.util.matcher.HamcrestMatchers.closeTo

class VideoServiceTest extends Specification {
    def "returns the video slider position minus 10 seconds"(int a, int b, int c, double d) {
        given: "a media player with some values"
        Duration totalDuration = Duration.seconds(a);
        Duration currentDuration = Duration.seconds(b);

        when:
        VideoService service = new VideoService();
        Double result = service.getPositionSecondsBefore(totalDuration, currentDuration, c);

        then:
        result closeTo(d, 0.00001d)

        where:
        a | b | c | d
        120 | 60 | 4 | 46.666666
        120 | 60 | 10 | 41.66666
        1457 | 457 | 23 | 29.787234
        123 | 11 | 23 | 0.0
    }

    def "returns a formatted string hh:mm:ss from a duration" (int a, String expected) {
        given:
        Duration duration = Duration.seconds(a)

        when:
        VideoService service = new VideoService();
        String result = service.formatDuration(duration);

        then:
        result == expected

        where:
        a | expected
        43932 | "12:12:12"
        21276 | "05:54:36"
        12 | "00:00:12"
    }
}
