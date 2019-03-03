package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.video.category.CategoryGroup;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class CategoryModel extends AbstractGriffonModel {
    private final HashMap<Category, CategoryGroup> categoryList = new HashMap<>();
    private HashMap<Category, SimpleLongProperty> categoryPropertyList;
    private SimpleIntegerProperty total;

    @MVCMember @Nonnull private Video video;

    public long getTotal() {
        return total.get();
    }
    public SimpleIntegerProperty totalProperty() {
        return total;
    }

    public void generateProperties() {
        categoryPropertyList = new HashMap<>();

        video.getCollection().getCategorySet().forEach(category -> {
            long total = video.getPointSet().stream().filter(point -> {
                return point.getCategory().equals(category);
            }).count();

            categoryPropertyList.put(category, new SimpleLongProperty(this, category.getName(), total));
        });

        total = new SimpleIntegerProperty(this, "total", video.getPointSet().size());
    }

    public SimpleLongProperty getCategoryProperty(Category category){
        return categoryPropertyList.get(category);
    }
    public HashMap<Category, SimpleLongProperty> getCategoryPropertyList() {
        return categoryPropertyList;
    }

    public Set<Category> getCategorySet() {
        return this.video.getCollection().getCategorySet();
    }
    public void setVideo(Video video) {
        this.video = video;
    }

    public void addPoint(Point point) {
        SimpleLongProperty spl = getCategoryProperty(point.getCategory());
        spl.setValue(spl.getValue() + 1);
        total.setValue(total.getValue() + 1);
    }
}