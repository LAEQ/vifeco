package org.laeq.graphic.icon;

import javafx.geometry.Point2D;
import org.laeq.model.Category;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CategoryMatrice {
    private double width;
    private double height;
    private int size;
    private IconType type;

    Set<Category> categorySet;
    Point2D[] points;

    Map<Category, IconAbstract> iconMap = new HashMap<>();

    public CategoryMatrice(Set<Category> categorySet, IconType type) {
        this.type = type;
        this.setValues();

        this.categorySet = categorySet;
        points = new Point2D[categorySet.size()];
        generatePositions();

        int index = 0;
        for (Category category: categorySet) {
            IconAbstract icon = generateIcon(category);
            icon.setPosition(points[index]);
            index++;
            iconMap.put(category, icon);
        }
    }

    private IconAbstract generateIcon(Category category){
        if(this.type == IconType.COUNT){
            return new CategoryIconCount(category, this.size, this.width, this.height);
        } else {
            return new CategoryIcon(category, this.width, this.height);
        }
    }

    private void setValues(){
        if(type == IconType.COUNT){
            this.width = 170;
            this.height = 60;
            this.size = 50;
        } else if(type == IconType.REGULAR){
            this.width = 180;
            this.height  = 30;
        }
    }

    public Map<Category, IconAbstract> getIconMap() {
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
