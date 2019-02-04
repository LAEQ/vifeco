package org.laeq.model;

import javafx.util.Duration;

public class Point implements Comparable<Point> {
    private int id;
    private double x;
    private double y;
    private Duration start;
    private User user;
    private Category category;
    private Video video;

    public Point() {
    }

    public Point(int id, double x, double y, Duration start, Video video, User user, Category category) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.start = start;
        this.user = user;
        this.category = category;
        this.video = video;
    }

    public Point(double x, double y, Duration start, Video video, User user, Category category) {
        this.x = x;
        this.y = y;
        this.start = start;
        this.user = user;
        this.category = category;
        this.video = video;
    }

    public Video getVideo() {
        return video;
    }
    public void setVideo(Video video) {
        this.video = video;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public Duration getStart() {
        return start;
    }
    public void setStart(Duration start) {
        this.start = start;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int compareTo(Point o) {
        return this.start.compareTo(o.start);
    }
}

