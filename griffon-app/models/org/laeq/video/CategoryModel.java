package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.video.player.VideoEditor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class CategoryModel extends AbstractGriffonModel {
    @MVCMember @Nonnull private VideoEditor editor;

    private final HashMap<Category, SimpleLongProperty> categoryPropertyList = new HashMap<>();
    private final SimpleIntegerProperty total = new SimpleIntegerProperty(this, "total", 0);

    public long getTotal() {
        return total.get();
    }
    public SimpleIntegerProperty totalProperty() {
        return total;
    }

    public void generateProperties() {
//        editor.getVideo().getCollection().getCategorySet().forEach(category -> {
//            long total = editor.getVideo().getPointSet().stream().filter(point -> point.getCategory().equals(category)).count();
//
//            categoryPropertyList.put(category, new SimpleLongProperty(this, category.getName(), total));
//        });
//
//        total.set(editor.getVideo().getPointSet().size());
    }

    public SimpleLongProperty getCategoryProperty(Category category){
        return categoryPropertyList.get(category);
    }

    public Set<Category> getCategorySet() {
        return this.editor.getVideo().getCollection().getCategorySet();
    }

    public void addPoint(Point point) {
//        SimpleLongProperty spl = getCategoryProperty(point.getCategory());
//
//        if(spl != null){
//            long value = spl.getValue();
//            spl.setValue(value + 1);
//            value = total.getValue();
//            total.setValue(value + 1);
//        } else {
//            getLog().error("No spl for: " + point.getCategory());
//        }
    }

    public void deletePoint(Point point) {
//        SimpleLongProperty spl = getCategoryProperty(point.getCategory());
//        long value = spl.getValue();
//        spl.setValue(value - 1);
//        value = total.getValue();
//        total.setValue(value +- 1);
    }
}