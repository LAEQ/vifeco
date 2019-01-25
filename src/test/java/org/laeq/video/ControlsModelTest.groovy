package org.laeq.video

import spock.lang.Ignore
import spock.lang.Specification

import static spock.util.matcher.HamcrestMatchers.closeTo

class ControlsModelTest extends Specification {
    @Ignore
    def "increaseRate() should increase the rate by 0.1 to a max of 10.0"(double a, double b, String c) {
        given:
        def controlsModel = new ControlsModel();

        expect:
        controlsModel.setRate(a);
        controlsModel.increaseRate();
        def rate = controlsModel.getRate();
        def spinnerRate = controlsModel.getSpinnerRate()
        rate closeTo(b, 0.000001d)
        spinnerRate == c

        where:
        a | b | c
        2.3d | 2.4d | "2.4"
        5.2d | 5.3d | "5.3"
        10.0d | 10.0d | "10.0"
    }

    @Ignore
    def "decreaseRate should decrease the rate by 0.1 to a min of 0.1"(double a, double b, String c) {
        given:
        def controlsModel = new ControlsModel();

        expect:
        controlsModel.setRate(a);
        controlsModel.decreateRate();
        def rate = controlsModel.getRate()
        def spinnerRate = controlsModel.getSpinnerRate();
        rate closeTo(b, 0.000001d)
        spinnerRate == c

        where:
        a | b | c
        2.3d | 2.2d | "2.2"
        5.2d | 5.1d | "5.1"
        0.1d | 0.1d | "0.1"
    }
}
