package org.laeq.statistic;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.statistic.Edge;
import org.laeq.model.statistic.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticTimeline extends Group {
    private Duration duration;
    private final ArrayList<Line> lines = new ArrayList<>();
    private final ArrayList<Text> texts = new ArrayList<>();
    private final Group vertices = new Group();
    private final Group edges = new Group();
    private final int ratio = 50;
    private final double height = 100;
    private double y = 160;
    private Map<Category, Graph> graphMap;
    private Video videoA;
    private Video videoB;

    BidiMap<Point, Circle> circlePointMap = new DualHashBidiMap<>();

    public StatisticTimeline() {
        getChildren().addAll(edges, vertices);
    }

    public Group getVertices(){
        return vertices;
    }

    public void init(Video videoA, Video videoB) {
        this.videoA = videoA;
        this.videoB = videoB;
        this.duration = Duration.millis(videoA.getDuration());
        for (int i = 0; i < this.duration.toSeconds(); i++) {
            lines.add(drawLine(i));
            if (i % 5 == 0) {
                texts.add(drawText(i));
            }
        }

        Line axis = new Line();
        axis.setStroke(Color.GRAY);
        axis.setStartX(0);
        axis.setEndX(this.duration.toSeconds() * this.ratio);
        axis.setStartY(height);
        axis.setEndY(height);

        vertices.getChildren().clear();
        vertices.getChildren().add(axis);
        vertices.getChildren().addAll(lines);
        vertices.getChildren().addAll(texts);
    }

    public void setX(double x){
        this.setLayoutX(x);
    }

    public double getRatio() {
        return ratio;
    }


    private Line drawLine(int x) {
        double height = (x % 10 == 0) ? (this.height - 10) : (this.height - 5);

        Line line = new Line(x * ratio, height, x * ratio, this.height);
        line.setStroke(Color.GRAY);
        line.setStrokeWidth(1);
        return line;
    }
    private String formatText(int value) {
        StringBuilder builder = new StringBuilder();

        int hours = value / 3600;
        int minutes = (value - (hours * 3600)) / 60;
        int seconds = (value - (hours * 3600)) % 60;

        if (hours > 0) {
            builder.append(hours + ":");
        }

        if (minutes > 0) {
            builder.append(minutes + ":");
        }

        builder.append(seconds);

        return builder.toString();
    }
    private Text drawText(int i) {
        Text text = new Text(formatText(i));
        text.setLayoutX(i * ratio - 2);
        text.setLayoutY(this.height - 15);
        text.setFill(Color.DARKGRAY);
        text.setFont(new Font("sans", 10));
        return text;
    }

    public void setGraphs(Map<Category, Graph> graphMap) {
        this.graphMap = graphMap;
    }

    public void display(Category category){
        Graph graph = this.graphMap.get(category);

//        graph.vertices.keySet().forEach(point -> {
//            Circle circle = new Circle(-2,-2,4);
//            circle.setFill(Color.GRAY);
//
//            double y = point.getVideo().equals(videoA)? 10: 70;
//
//            circle.setLayoutY(y);
//            circle.setLayoutX(point.getStartDouble() / 1000 * this.ratio);
//
//            circlePointMap.put(point, circle);
//
//            vertices.getChildren().add(circle);
//        });


        Set<Edge> edgeSet =  graph.edges.values().stream().flatMap(List::stream).collect(Collectors.toSet());

        edgeSet.forEach(edge -> {
            Line line = new Line();

            Circle start = circlePointMap.get(edge.start.point);
            Circle end = circlePointMap.get(edge.end.point);

            line.setStartX(start.getLayoutX());
            line.setStartY(start.getLayoutY());

            line.setEndX(end.getLayoutX());
            line.setEndY(end.getLayoutY());

            edges.getChildren().add(line);
        });
    }

    public void drawDots(Category category) {
        Graph graph = this.graphMap.get(category);

//        graph.vertices.keySet().forEach(point -> {
//            Circle circle = new Circle(-2,-2,4);
//            circle.setFill(Color.GRAY);
//
//            double y = point.getVideo().equals(videoA)? 10: 70;
//
//            circle.setLayoutY(y);
//            circle.setLayoutX(point.getStartDouble() / 1000 * this.ratio);
//
//            circlePointMap.put(point, circle);
//
//            vertices.getChildren().add(circle);
//        });
    }

    public void drawEdges(Collection<Edge> values) {
        values.stream().forEach(edge -> {
            Line line = new Line();

            Circle start = circlePointMap.get(edge.start.point);
            Circle end = circlePointMap.get(edge.end.point);

            line.setStartX(start.getLayoutX());
            line.setStartY(start.getLayoutY());

            line.setEndX(end.getLayoutX());
            line.setEndY(end.getLayoutY());

            edges.getChildren().add(line);
        });
    }
}
