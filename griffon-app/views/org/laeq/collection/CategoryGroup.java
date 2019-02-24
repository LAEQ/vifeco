package org.laeq.collection;

import javafx.scene.Group;
import org.laeq.model.Category;

import java.util.Set;

public class CategoryGroup extends Group {
    private Set<Category> categorySet;

    /**
     * Constructs a group.
     */
    public CategoryGroup(Set<Category> categorySet) {
        this.categorySet = categorySet;

        int index = 0;
        int x = 0;
        int y = 0;
        for (Category category : categorySet) {

            CategoryIcon group = new CategoryIcon(category);

            group.setLayoutX(x);
            group.setLayoutY(y);

            x += group.getWidth() + 5;
            index++;
            if (index != 0 && index % 4 == 0) {
                x = 0;
                y += 35;
            }

            getChildren().add(group);
        }
    }



    public CategoryGroup() {

    }
}
