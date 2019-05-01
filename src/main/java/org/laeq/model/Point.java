package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javafx.util.Duration;
import org.laeq.model.serializer.PointDeserializer;

import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties({"video", "icon", "duration", "createdAt", "updatedAt", "category", "uuid"})
@JsonPropertyOrder({"id", "x", "y", "categoryId", "startDouble", "videoId"})
@JsonDeserialize(using = PointDeserializer.class)
public class Point extends BaseEntity implements Comparable<Point> {
    private int id;
    private double x;
    private double y;
    private Duration start;
    private Category category;
    private Video video;
    private UUID uuid = UUID.randomUUID();

    public Point() {}

    public Point(int id) {
        this.id = id;
    }

    public Point(int id, Duration start){
        this.id = id;
        this.start = start;
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

    public String getVideoId(){
        return video.getUuid().toString();
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
        if(uuid.compareTo(o.uuid) == 0){
            return 0;
        }

        int compare = this.start.compareTo(o.start);
        if(compare == 0){
            return uuid.compareTo(o.uuid);
        }

        return compare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return uuid.compareTo(point.uuid) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Point("+ id +":" + start +')';
    }

    public int getCategoryId(){
        return category.getId();
    }
}

