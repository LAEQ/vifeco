package org.laeq.model.comparator;

import org.laeq.model.Point;

import java.util.Comparator;

public class DurationComparator implements Comparator<Point> {
    @Override
    public int compare(Point o1, Point o2) {
        return o1.getStart().lessThanOrEqualTo(o2.getStart()) ? -1 : 1;
    }

    @Override
    public Comparator<Point> reversed() {
        return null;
    }
}
