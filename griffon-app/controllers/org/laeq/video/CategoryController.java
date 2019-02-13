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
import org.laeq.model.VideoUser;
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
        getApplication().getEventRouter().addEventListener(listeners());
    }


    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("player.video_user.load", objects -> {
            VideoUser videoUser = (VideoUser) objects[0];
            model.setItem(videoUser);
            view.initView();
        });

        return list;
    }
}