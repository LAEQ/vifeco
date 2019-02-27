package org.laeq.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.Objects;

public class Point extends BaseEntity implements Comparable<Point> {
    private int id;
    private double x;
    private double y;
    private Duration start;
    private Category category;
    private Video video;
    private Icon icon;

    public Point() {
    }

    public Point(int id, double x, double y, Duration start, Video video, Category category) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.start = start;
        this.category = category;
        this.video = video;
    }

    public Point(double x, double y, Duration start, Video video,Category category) {
        this.x = x;
        this.y = y;
        this.start = start;
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
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int compareTo(Point o) {
        if(id == o.id && id != 0){
            return 0;
        }

        int compare = this.start.compareTo(o.start);
        if(compare == 0){
            return id - o.id;
        }

        return compare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return id == point.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", category=" + category +
                ", video=" + video +
                '}';
    }

    public Icon getIcon(Bounds bounds) {
        if(this.icon == null){
            this.icon = new Icon(category, 100);
        }

        this.icon.setLayoutX(getX() * bounds.getWidth() - 100 / 2);
        this.icon.setLayoutY(getY() * bounds.getHeight() - 100 / 2);

        return icon;
    }


    private double getIconX(Double value){
        return getX() * value - 100 / 2;
    }

    private double getIconY(Double value){
        return getY() * value - 100 / 2;
    }

    public Icon getIcon() {
        if(this.icon == null){
            this.icon = new Icon(category, 100);
        }

        return icon;
    }

    public void repositionY(Double newValue) {
        this.icon.setLayoutY(getY() * newValue - 100 / 2);
    }

    public void repositionX(Double newValue) {
        this.icon.setLayoutX(getX() * newValue - 100 / 2);
    }
}

