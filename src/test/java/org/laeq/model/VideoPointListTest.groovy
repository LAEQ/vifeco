package org.laeq.model

import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.scene.Group
import javafx.scene.layout.Pane
import javafx.util.Duration
import spock.lang.Specification

class VideoPointListTest extends Specification {
    def "update a Pane with the according videoPoint elements"() {
        given: "a pane which displays the video point list"
        Pane pane = new Pane();
        Category cat = new Category("a", "a", "a")
        VideoPoint point1 = generatePoint(10, 10, 10, 10, Duration.seconds(100), cat)
        VideoPoint point2 = generatePoint(10, 10, 10, 10, Duration.seconds(105), cat)
        VideoPoint point3 = generatePoint(10, 10, 10, 10, Duration.seconds(107), cat)

        when:
        VideoPointList list = new VideoPointList()
        list.init(pane);
        list.addVideoPoint(point1)
        list.addVideoPoint(point2)
        list.addVideoPoint(point3)
        list.updatePane(Duration.seconds(111))

        ObservableList<VideoPoint> pointList = list.getPointList()
        ObservableMap<VideoPoint, Group> displayPointLit = list.getDisplayPointMap()

        then:
        list.getPointList().size() == 3
        list.getDisplayPointMap().keySet().size() ==
        list.getDisplayPointMap().keySet().contains(point1) == false
        list.getDisplayPointMap().keySet().contains(point2) == true
        list.getDisplayPointMap().keySet().contains(point3) == true
        pane.getChildren().size() == 2
    }

    def "update a Pane with more videoPoint element"(int total, int duration, int start, int now, int size_expected) {
        given: "300 video points with a duration of 60 seconds. Test points @"
        Pane pane = new Pane();
        Category cat = new Category("a", "a", "a")
        ArrayList<VideoPoint> list = new ArrayList<>();

        for(int i = 0 ; i < total; i++){
            list.add(generatePoint(0,0,10, duration, Duration.seconds(start * i), cat))
        }

        when:
        VideoPointList service = new VideoPointList()
        service.init(pane)

        list.each { service.addVideoPoint(it) }
        service.updatePane(Duration.seconds(now))

        then:
        service.getDisplayPointMap().size() == size_expected
        pane.getChildren().size() == size_expected

        where:
        total | duration | start | now | size_expected
        300   | 60       | 3     | 600 | 21
        2000  | 20       | 1     | 253 | 21

    }

    def VideoPoint generatePoint(double x, double y, int size, int duration, Duration start, Category cat){
        return new VideoPoint(x, y, size, duration, start, cat)
    }
}
