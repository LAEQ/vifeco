package org.laeq.model.comparator;

import org.laeq.model.Point;

import java.util.Comparator;

public class CategoryComparator implements Comparator<Point> {
    @Override
    public int compare(Point o1, Point o2) {
        int result = o1.getCategory().compareTo(o2.getCategory());
        if(result == 0){
            return o1.getStart().lessThanOrEqualTo(o2.getStart()) ? -1 : 1;
        } else {
            return result;
        }
    }

    @Override
    public Comparator<Point> reversed() {
        return null;
    }
}
