package org.laeq.video.player;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.SetChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.laeq.graphic.icon.TimelineIcon;
import org.laeq.model.Point;
import org.laeq.model.icon.IconSize;
import org.laeq.model.icon.IconTimeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class VideoTimeline extends Group {
    private Duration duration;
    private final ArrayList<Line> lines = new ArrayList<>();
    private final ArrayList<Text> texts = new ArrayList<>();
    private final Group group = new Group();
    private final int ratio = 50;
    private final double height = 160;
    private final int iconSize = 17;
    private final HashMap<IconTimeline, Integer> icons = new HashMap<>();


    private double y = 160;

    private TranslateTransition translate;
    private Boolean isPlaying = false;
    private SetChangeListener<Point> listener;

    private Duration currentPosition = Duration.seconds(0);

    public VideoTimeline() {
        getChildren().add(group);

        setCache(true);
        setCacheHint(CacheHint.SPEED);
    }

    public Group getGroup(){
        return group;
    }

    private double getNextY(){
        if(y >= height - 17){
            y = 17;
        } else {
            y += iconSize + 5;
        }

        return y;
    }

    public int getIconIdentifier(Group group){
        return icons.get(group);
    }

    public void addPoint(Point point) {
        double x = point.getStart().toSeconds() * ratio;

        IconTimeline icon = new IconTimeline(new IconSize(point.getCategory(), iconSize), point.getId());
        icon.decorate();
        icon.position(new Point2D(x, getNextY()));

        icons.put(icon, icon.getIdentifier());

//        TimelineIcon icon = new TimelineIcon(x, getNextY(), iconSize, point);

        group.getChildren().add(icon);
    }

    public void removePoint(TimelineIcon icon){
        group.getChildren().remove(icon);
    }

    public void tooglePlay(){
        if(isPlaying){
            isPlaying = false;
            this.currentPosition = translate.getCurrentTime();
            this.translate.pause();

        } else {
            isPlaying = true;
            translate.playFrom(this.currentPosition);
        }
    }

    public void setX(double x){
        this.setLayoutX(x);
    }

    public void init(Duration duration) {
        this.duration = duration;
        for (int i = 0; i < this.duration.toSeconds(); i++) {
            lines.add(drawLine(i));
            if (i % 5 == 0) {
                texts.add(drawText(i));
            }
        }


        translate = new TranslateTransition(this.duration, this.group);
        translate.setInterpolator(Interpolator.LINEAR);
        translate.setFromX(0);
        translate.setToX(0 - (this.duration.toSeconds() * this.ratio));
        translate.setCycleCount(1);
        translate.setAutoReverse(false);

        group.getChildren().clear();

        group.getChildren().addAll(lines);
        group.getChildren().addAll(texts);
    }

    private void initListener() {
        listener = listener();
//        points.addListener(listener);
    }
    private void removeListener() {
//        points.removeListener(listener);
    }

    private SetChangeListener<Point> listener() {
        return change -> {

        };
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

    public void translate(Duration position) {
        this.translate.pause();
        this.currentPosition = position;
        this.group.setTranslateX(- this.currentPosition.toSeconds() * this.ratio);

        if(isPlaying){
            this.translate.playFrom(this.currentPosition);
        }
    }

    public double getRatio() {
        return ratio;
    }

    public void setRate(double rate) {
        translate.setRate(rate);
    }
}
