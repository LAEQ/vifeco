package org.laeq.graphic;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.laeq.model.Icon;
import org.laeq.model.Point;
import org.laeq.model.icon.IconPointColorized;

import java.util.ArrayList;

public class Timeline extends Group {
    private final Duration duration;
    private final ObservableSet<Point> points = FXCollections.observableSet();
    private final ArrayList<Line> lines = new ArrayList<>();
    private final ArrayList<Text> texts = new ArrayList<>();
    private final Group group = new Group();
    private final int ratio = 50;
    private double y = 81;

    private TranslateTransition translate;
    private Boolean isPlaying = false;
    private SetChangeListener<Point> listener;

    public Timeline(Duration duration) {
        this.duration = duration;

        init();
        initListener();

        getChildren().add(group);

        setCache(true);
        setCacheHint(CacheHint.SPEED);
    }

    private double getNextY(){
        if(y > 56){
            y = 0;
        } else {
            y += 15;
        }

        return y;
    }
    public void addPoint(Point point) {
        IconPointColorized icon = point.getIcon();
        double x = point.getStart().toSeconds() * ratio;
        icon.setLayoutX(x);
        icon.setLayoutY(getNextY());
        group.getChildren().add(point.getIcon());
    }
    public void tooglePlay(){
        if(isPlaying){
            isPlaying = false;
            translate.pause();
        } else {
            isPlaying = true;
            translate.playFrom(this.translate.getCurrentTime());
        }
    }

    private void init() {
        for (int i = 0; i < this.duration.toSeconds(); i++) {
            lines.add(drawLine(i));
            if (i % 5 == 0) {
                texts.add(drawText(i));
            }
        }

        group.getChildren().addAll(lines);
        group.getChildren().addAll(texts);

        translate = new TranslateTransition(duration, this);
        translate.setInterpolator(Interpolator.LINEAR);
        translate.setFromX(this.getLayoutX());
        translate.setToX(this.getLayoutX() - (this.duration.toSeconds() * this.ratio));
        translate.setCycleCount(1);
        translate.setAutoReverse(false);
    }

    private void initListener() {
        listener = listener();
        points.addListener(listener);
    }
    private void removeListener() {
        points.removeListener(listener);
    }
    private SetChangeListener<Point> listener() {
        return change -> {


        };
    }

    private Line drawLine(int x) {
        int height = (x % 10 == 0) ? 10 : 5;

        Line line = new Line(x * ratio, 99, x * ratio, 99 - height);
        line.setStroke(javafx.scene.paint.Color.GRAY);
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
        text.setLayoutY(85);
        text.setFill(Color.DARKGRAY);
        text.setFont(new Font("sans", 10));
        return text;
    }
}



