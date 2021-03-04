package org.laeq.model.statistic


import org.laeq.model.Point
import org.laeq.model.Video
import org.laeq.service.VideoGenerator
import spock.lang.Specification

class MatchedPointTest extends Specification {
    Video video1, video2
    void setup() {
        video1 = VideoGenerator.generateVideo(1)
        video2 = VideoGenerator.generateVideo(1)

    }

    def "matched is true"() {
        when:
        Point point1 = new Point();
        point1.video = video1
        Point point2 = new Point();
        point2.video = video2
        MatchedPoint mp = new MatchedPoint();
        mp.pt1 = point1
        mp.pt2 = point2

        then:
        mp.matched() == true
    }

    def "matched is false"() {
        when:
        Point point1 = new Point();
        point1.video = video1
        MatchedPoint mp = new MatchedPoint();
        mp.pt1 = point1
        mp.pt2 = null

        then:
        mp.matched() == false
    }

    def "matched is false again"() {
        when:
        Point point1 = new Point();
        point1.video = video1
        MatchedPoint mp = new MatchedPoint();
        mp.pt1 = null
        mp.pt2 = point1

        then:
        mp.matched() == false
    }

    def "UnmatchedVideo 1 is true"() {
        when:
        Point point1 = new Point();
        point1.video = video1
        MatchedPoint mp = new MatchedPoint();
        mp.pt1 = point1
        mp.pt2 = null

        then:
        mp.unmatchedVideo1() == true
    }

    def "UnmatchedVideo2 is true"() {
        when:
        Point point1 = new Point();
        point1.video = video1
        Point point2 = new Point();
        point2.video = video2
        MatchedPoint mp = new MatchedPoint();
        mp.pt1 = null
        mp.pt2 = point2

        then:
        mp.unmatchedVideo2() == true
    }

    def "get point from matched"() {
        when:
        Point point1 = new Point();
        point1.video = video1
        Point point2 = new Point();
        point2.video = video2
        MatchedPoint mp = new MatchedPoint();
        mp.pt1 = point1
        mp.pt2 = point2

        then:
        mp.getPoint() == point1
    }

    def "get point from unmatched video 1"() {
        when:
        Point point = new Point();
        point.video = video1
        MatchedPoint mp = new MatchedPoint();
        mp.pt1 = point
        mp.pt2 = null

        then:
        mp.getPoint() == point
    }

    def "get point from unmatched video 2"() {
        when:
        Point point = new Point();
        point.video = video1
        MatchedPoint mp = new MatchedPoint();
        mp.pt1 = null
        mp.pt2 = point

        then:
        mp.getPoint() == point
    }
}
