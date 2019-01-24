package org.laeq.model;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.Objects;

public class VideoPoint {
    private final Point2D point;
    private final int size;
    private final int duration;
    private final Duration start;
    private final Duration end;
    private final Category category;
    private final CategoryIcon icon;

    public VideoPoint(Point2D point, int size, int duration, Duration start, Category category, CategoryIcon icon) {
        this.point = point;
        this.size = size;
        this.duration = duration;
        this.start = start;
        this.end = Duration.seconds(start.toSeconds() + duration);
        this.category = category;
        this.icon = icon;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoPoint that = (VideoPoint) o;
        return size == that.size &&
                duration == that.duration &&
                point.equals(that.point) &&
                start.equals(that.start) &&
                Objects.equals(end, that.end) &&
                category.equals(that.category) &&
                Objects.equals(icon, that.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, start, category);
    }
}
