package org.laeq.video;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.beans.property.SimpleIntegerProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Point;
import org.laeq.model.VideoPoint;
import org.laeq.video.category.CategoryView;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

@ArtifactProviderFor(GriffonController.class)
public class CategoryController extends AbstractGriffonController {

    @MVCMember @Nonnull private CategoryModel model;
    @MVCMember @Nonnull private CategoryView view;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {



//        getApplication().getEventRouter().addEventListener("video.point.create", videoPoints -> {
//            runInsideUIAsync(() -> {
//                VideoPoint vp = (VideoPoint) videoPoints[0];
//                SimpleIntegerProperty property = model.getCategoryProperty(vp.getCategory());
//                property.set(property.getValue() + 1);
//                model.setTotal(model.totalProperty().getValue() + 1);
//            });
//        });

        getApplication().getEventRouter().addEventListener(listeners());
    }



    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("video.load.points", objects -> {
            SortedSet<Point> points = (SortedSet<Point>) objects[0];

            model.setItems(points);
        });


        return list;
    }


}