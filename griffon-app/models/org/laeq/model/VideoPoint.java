package org.laeq.model;

import javafx.util.Duration;

import java.util.Objects;

public class VideoPoint {
    private final double x;
    private final double y;
    private final int size;
    private final int duration;
    private final Duration start;
    private final Duration end;
    private final Category category;

    public VideoPoint(double x, double y, int size, int duration, Duration start, Category category) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.duration = duration;
        this.start = start;
        this.end = Duration.seconds(start.toSeconds() + duration);
        this.category = category;
    }

    public Boolean isValid(Duration now){
        return this.end.greaterThanOrEqualTo(now) && this.start.lessThanOrEqualTo(now);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoPoint that = (VideoPoint) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0 &&
                size == that.size &&
                duration == that.duration &&
                Objects.equals(start, that.start) &&
                Objects.equals(end, that.end) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, size, duration, start, end, category);
    }
}
