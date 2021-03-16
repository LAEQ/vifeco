package org.laeq.model.comparator;

import org.laeq.model.Point;

import java.util.Comparator;

public class CategoryComparator implements Comparator<Point> {
    @Override
    public int compare(Point o1, Point o2) {
        return o1.getCategory().compareTo(o2.getCategory());
    }

    @Override
    public Comparator<Point> reversed() {
        return null;
    }
}
