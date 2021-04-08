package org.laeq.editor;

import org.laeq.model.Point;
import org.laeq.model.icon.IconPointColorized;

import java.util.*;

public class PointCollection {
    public NavigableSet<Point> points = new TreeSet<>();
    public Map<String, Point> pointsMap = new HashMap<>();

    public PointCollection() {}

    public void setPoints(List<Point> points){
        points.forEach(point -> {
            this.points.add(point);
            this.pointsMap.put(point.getId().toString(), point);
        });
    }

    public Point getIcon(String key) {
        return pointsMap.get(key);
    }

    public Collection<Point> subList(Point start, Point end) {
        return points.subSet(start, true, end, false);
    }

    public void addPoint(Point point) {
        pointsMap.put(point.getId().toString(), point);
        points.add(point);
    }

    public Optional<Point> getPointFromIcon(IconPointColorized icon) {
        return points.parallelStream().filter(pt -> pt.getIconPoint().equals(icon)).findFirst();
    }

    public void removePoint(Point point) {
        this.points.remove(point);
        this.pointsMap.remove(point.getId().toString());
    }
}
