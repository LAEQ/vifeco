package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.geometry.Bounds;
import javafx.util.Duration;

import java.util.Objects;
@JsonIgnoreProperties({"id", "video", "icon", "duration", "createdAt", "updatedAt"})
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

    public double getStartDouble(){
        return start.toMillis();
    }

    @JsonIgnore
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
                "id=" + id +
                ", start=" + start +
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

    public Icon getIcon() {
        if(this.icon == null){
            this.icon = new Icon(category, 100);
        }

        return icon;
    }

    @JsonIgnore
    public void repositionY(Double newValue) {
        this.icon.setLayoutY(getY() * newValue - 100 / 2);
    }
    @JsonIgnore
    public void repositionX(Double newValue) {
        this.icon.setLayoutX(getX() * newValue - 100 / 2);
    }
}

