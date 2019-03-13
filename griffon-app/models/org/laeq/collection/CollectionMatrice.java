package org.laeq.collection;

import javafx.geometry.Point2D;
import org.laeq.model.Category;
import org.laeq.model.CategoryIcon;
import org.laeq.model.icon.IconCounter;
import org.laeq.model.icon.IconSize;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CollectionMatrice {
    private double width;
    private double height;

    Set<Category> categorySet;
    Point2D[] points;

    Map<Category, CategoryIcon> iconMap = new HashMap<>();

    public CollectionMatrice(double width, double height, Set<Category> categorySet) {
        this.width = width;
        this.height = height;
        this.categorySet = categorySet;
        points = new Point2D[categorySet.size()];
        generatePositions();

        int index = 0;
        for (Category category : categorySet) {
            IconCounter iconCounter = new IconCounter(new IconSize(category, 50), width, height);
            CategoryIcon icon = new CategoryIcon(category, 50, this.width, this.height);
            icon.setPosition(points[index]);
            index++;
            iconMap.put(category, icon);
        }

    }

    public Map<Category, CategoryIcon> getIconMap() {
        return iconMap;
    }

    private void generatePositions() {
        double x = 0;
        double y = 0;
        for (int i = 1; i < categorySet.size() + 1; i++) {
            Point2D point = new Point2D(x, y);

            x += width + 10;

            if (i % 3 == 0) {
                x = 0;
                y += height + 10;
            }

            points[i - 1] = point;
        }
    }
}
