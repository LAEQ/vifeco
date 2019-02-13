package org.laeq.model;

import javafx.geometry.Bounds;
import javafx.util.Duration;

public class VideoPoint implements Comparable<VideoPoint>{
    private  int size;
    private  int duration;
    private  Duration start;
    private  Duration end;
    private  Category category;
    private  CategoryIcon icon;
    private Point point;

    public VideoPoint(int size, int duration, CategoryIcon icon, Point point) {
        this.point = point;
        this.size = size;
        this.duration = duration;
        this.icon = icon;
        this.point = point;
        this.start = point.getStart();
        this.end = Duration.seconds(start.toSeconds() + duration);
    }

    public double getIconX(Bounds bounds){
        return point.getX() * bounds.getWidth() - (icon.getWidth() / 2);
    }
    public double getIconY(Bounds bounds){
        return point.getY() * bounds.getHeight() - (icon.getHeight() / 2);
    }

    public CategoryIcon getIcon() {
        return icon;
    }
    public Boolean isValid(Duration now){
        return this.end.greaterThanOrEqualTo(now) && this.start.lessThanOrEqualTo(now);
    }

    @Override
    public String toString() {
        return "VideoPoint{" +
                "point=" + point +
                ", start=" + start +
                ", category=" + category +
                '}';
    }
    public Category getCategory() {
        return point.getCategory();
    }

    @Override
    public int compareTo(VideoPoint vp) {
       return this.point.compareTo(vp.point);
    }
}
