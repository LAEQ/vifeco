package org.laeq.video

import spock.lang.Specification
import static spock.util.matcher.HamcrestMatchers.closeTo


class ControlsModelTest extends Specification {
    def "increaseRate() should increase the rate by 0.1 to a max of 10.0"(double a, double b) {
        given:
        def controlsModel = new ControlsModel();

        expect:
        controlsModel.setRate(a);
        controlsModel.increaseRate();
        def rate = controlsModel.getRate();
        rate closeTo(b, 0.000001d)

        where:
        a | b
        2.3d | 2.4d
        5.2d | 5.3d
        10.0d | 10.0d
    }

    def "decreaseRate should decrease the rate by 0.1 to a min of 0.1"(double a, double b) {
        given:
        def controlsModel = new ControlsModel();

        expect:
        controlsModel.setRate(a);
        controlsModel.decreateRate();
        def rate = controlsModel.getRate()
        rate closeTo(b, 0.000001d)

        where:
        a | b
        2.3d | 2.2d
        5.2d | 5.1d
        0.1d | 0.1d
    }
}
