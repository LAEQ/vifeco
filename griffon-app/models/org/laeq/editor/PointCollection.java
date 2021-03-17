package org.laeq.editor;

import org.laeq.model.Point;
import org.laeq.model.icon.IconPointColorized;

import java.util.*;
import java.util.stream.Collectors;

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

    public Collection<IconPointColorized> subList(Point start, Point end) {
        return points.subSet(start, true, end, false).stream().map(point -> point.getIconPoint()).collect(Collectors.toSet());
    }
}
