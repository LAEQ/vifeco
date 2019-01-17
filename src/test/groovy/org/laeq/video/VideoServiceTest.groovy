package org.laeq.video

import spock.lang.Specification
import java.time.Duration
import static spock.util.matcher.HamcrestMatchers.closeTo

class VideoCalculatorServiceTest extends Specification {
    def "returns the video slider position minus 10 seconds"(int a, int b, int c, double d) {
        given: "a media player with some values"
        Duration totalDuration = Duration.ofSeconds(a);
        Duration currentDuration = Duration.ofSeconds(b);

        when:
        VideoCalculatorService service = new VideoCalculatorService();
        Double result = service.getPositionSecondsBefore(totalDuration, currentDuration, c);

        then:
        result closeTo(d, 0.00001d)

        where:
        a | b | c | d
        120 | 60 | 4 | 46.666666
        120 | 60 | 10 | 41.66666
        1457 | 457 | 23 | 29.787234
    }
}
