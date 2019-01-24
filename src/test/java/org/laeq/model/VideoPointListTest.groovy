package org.laeq.model

import javafx.geometry.Point2D
import javafx.scene.layout.Pane
import javafx.util.Duration
import spock.lang.Specification

class VideoPointListTest extends Specification {
    def "update a Pane with the according videoPoint elements"() {
        given: "a pane which displays the video point list"
        Pane pane = new Pane();

        VideoPoint point1 = generatePoint(10, 10, 10, 10, Duration.seconds(100), new Category("a1", "a", "a"))
        VideoPoint point2 = generatePoint(10, 10, 10, 10, Duration.seconds(105), new Category("a2", "a", "a"))
        VideoPoint point3 = generatePoint(10, 10, 10, 10, Duration.seconds(107), new Category("a3", "a", "a"))

        when:
        VideoPointList list = new VideoPointList()
        list.init(pane);
        list.addVideoPoint(point1)
        list.addVideoPoint(point2)
        list.addVideoPoint(point3)
        list.update(Duration.seconds(111))


        then:
        list.getPointList().size() == 3
        list.getDisplayPoint().size() == 2
        pane.getChildren().size() == 2
    }

    def "update twice a Pane with the according videoPoint elements"() {
        given: "a pane which displays the video point list"
        Pane pane = new Pane();

        VideoPoint point1 = generatePoint(10, 10, 10, 10, Duration.seconds(10), new Category("a1", "a", "a1"))
        VideoPoint point2 = generatePoint(10, 10, 10, 10, Duration.seconds(12), new Category("a2", "a", "a2"))
        VideoPoint point3 = generatePoint(10, 10, 10, 10, Duration.seconds(15), new Category("a3", "a", "a3"))
        VideoPoint point4 = generatePoint(10, 10, 10, 10, Duration.seconds(21), new Category("a4", "a", "a4"))
        VideoPoint point5 = generatePoint(10, 10, 10, 10, Duration.seconds(23), new Category("a5", "a", "a5"))
        VideoPoint point6 = generatePoint(10, 10, 10, 10, Duration.seconds(24), new Category("a6", "a", "a6"))

        when:
        VideoPointList list = new VideoPointList()
        list.init(pane);
        list.addVideoPoint(point1)
        list.addVideoPoint(point2)
        list.addVideoPoint(point3)
        list.addVideoPoint(point4)
        list.addVideoPoint(point5)
        list.addVideoPoint(point6)
        list.update(Duration.seconds(24))
        list.update(Duration.seconds(21))

        then:
        list.getPointList().size() == 6
        list.getDisplayPoint().size() == 3
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
        service.update(Duration.seconds(now))

        then:
        service.getDisplayPoint().size() == size_expected
        pane.getChildren().size() == size_expected

        where:
        total | duration | start | now | size_expected
        300   | 60       | 3     | 600 | 21
        2000  | 20       | 1     | 253 | 21
    }

    def VideoPoint generatePoint(double x, double y, int size, int duration, Duration start, Category cat){
        PointIcon icon = new PointIcon(10, 10, "path/mock/image");
        return new VideoPoint(new Point2D(x, y), size, duration, start, cat, icon)
    }
}
