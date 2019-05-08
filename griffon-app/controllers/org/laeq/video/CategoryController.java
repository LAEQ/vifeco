package org.laeq.video;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.video.category.CategoryView;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class CategoryController extends AbstractGriffonController {
    @MVCMember @Nonnull private CategoryModel model;
    @MVCMember @Nonnull private CategoryView view;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
        model.generateProperties();
        view.initView();
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("point.added", objects -> {
            Point point = (Point) objects[0];
            runInsideUISync(() -> model.addPoint(point));
        });

        list.put("point.deleted", objects -> {
            runInsideUISync(() -> model.deletePoint((Point) objects[0]));
        });

        return list;
    }

    public void previousPoint(Category category) {
        getApplication().getEventRouter().publishEvent("point.go_previous", Arrays.asList(category));
    }

    public void nextPoint(Category category) {
        getApplication().getEventRouter().publishEvent("point.go_next", Arrays.asList(category));
    }
}