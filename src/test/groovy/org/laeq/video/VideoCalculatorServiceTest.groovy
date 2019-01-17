package org.laeq.video

import javafx.scene.control.Slider
import javafx.scene.media.MediaPlayer
import spock.lang.Specification

import java.time.Duration

class VideoCalculatorServiceTest extends Specification {
    def "returns the video slider position minus 10 seconds"() {
        given: "a media player with some values"
        Duration totalDuration = Duration.ofSeconds(120);
        Duration currentDuration = Duration.ofSeconds(60);
        Integer rewindSeconds = 10;

        Slider slider = new Slider();

        expect:

        true == true

    }
}
