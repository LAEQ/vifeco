package org.laeq.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.SortedSet;

public class VideoUser {
    private Video video;
    private User user;
    private IntegerProperty total;
    private DoubleProperty last;
    private SortedSet<Point> points;

    public VideoUser(Video video, User user) {
        this();
        this.video = video;
        this.user = user;
    }

    public VideoUser() {
        this.total = new SimpleIntegerProperty(this, "total", 0);
        this.last = new SimpleDoubleProperty(this, "last", 0.0);
    }
    public Video getVideo() {
        return video;
    }
    public void setVideo(Video video) {
        this.video = video;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public int getTotal() {
        return total.get();
    }
    public IntegerProperty totalProperty() {
        return total;
    }
    public void setTotal(int total) {
        this.total.set(total);
    }
    public double getLast() {
        return last.get();
    }
    public DoubleProperty lastProperty() {
        return last;
    }
    public void setLast(double last) {
        this.last.set(last);
    }

    @Override
    public String toString() {
        return "VideoUser{" +
                "video=" + video.getName() +
                ", user=" + user.getName().getValue() +
                ", total=" + total.getValue() +
                '}';
    }

    public void setPoints(SortedSet<Point> points) {
        this.points = points;
    }
    public SortedSet<Point> getPoints() {
        return points;
    }
}
