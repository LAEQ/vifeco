package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class CategoryModel extends AbstractGriffonModel {

    private HashMap<Category, SimpleIntegerProperty> categoryPropertyList;
    private SimpleIntegerProperty clickCount;

    @Nonnull
    public final SimpleIntegerProperty clickCountProperty() {
        if (clickCount == null) {
            clickCount = new SimpleIntegerProperty(this, "clickCount", 0);
        }
        return clickCount;
    }

    public int getClickCount() {
        return clickCount.get();
    }

    public void setClickCount(int clickCount) {
        this.clickCount.set(clickCount);
    }

    public void generateProperties(Set<Category> keySet) {

        categoryPropertyList = new HashMap<>();

        keySet.forEach(s -> {
            categoryPropertyList.put(s, new SimpleIntegerProperty(this, s.getName(), 0));
        });
    }

    public SimpleIntegerProperty getCategoryProperty(Category category){
        return categoryPropertyList.getOrDefault(category, clickCount);
    }
}