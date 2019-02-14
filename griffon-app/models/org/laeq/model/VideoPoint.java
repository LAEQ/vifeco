package org.laeq.model;

import java.util.Objects;

public class VideoPoint implements Comparable<VideoPoint>{
    private  int size;

    private  Icon icon;
    private Point point;

    public VideoPoint(int size, Icon icon, Point point) {
        this.point = point;
        this.size = size;
        this.icon = icon;
        this.point = point;
    }

    public Icon getIcon() {
        return icon;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return "VideoPoint{" +
                "point=" + point +
                '}';
    }
    public Category getCategory() {
        return point.getCategory();
    }

    @Override
    public int compareTo(VideoPoint vp) {
       return this.point.compareTo(vp.point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoPoint that = (VideoPoint) o;
        return point.equals(that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point);
    }
}
