package org.laeq.model.icon;

import javafx.geometry.Point2D;
import org.laeq.model.Category;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IconCounterMatrice {
    private final double width = 190;
    private final double height = 60;
    private final int size = 80;

    Set<Category> categorySet;
    Point2D[] points;

    Map<Category, IconCounter> iconMap = new HashMap<>();

    public IconCounterMatrice(Set<Category> categorySet) {
        this.categorySet = categorySet;
        points = new Point2D[categorySet.size()];
        generatePositions();

        int index = 0;
        for (Category category: categorySet) {
            IconCounter iconCounter = new IconCounter(new IconSize(category, size), width, height);
            iconCounter.decorate();
            iconCounter.position(points[index++]);
            iconMap.put(category, iconCounter);
        }
    }

    public Map<Category, IconCounter> getIconMap() {
        return iconMap;
    }

    private void generatePositions(){
        double x = 0;
        double y = 0;
        for (int i = 1; i < categorySet.size() + 1; i++) {
            Point2D point = new Point2D(x, y);

            x += width + 10;

            if(i % 3 == 0){
                x = 0;
                y += height + 10;
            }

            points[i - 1] = point;
        }
    }
}
