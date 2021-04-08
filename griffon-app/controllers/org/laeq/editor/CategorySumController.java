package org.laeq.editor;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Point;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class CategorySumController extends AbstractGriffonController {
    @MVCMember @Nonnull private CategorySumModel model;
    @MVCMember @Nonnull private CategorySumView view;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }

    @Override
    public void mvcGroupDestroy(){

    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("point.added", objects ->{
            model.addPoint((Point) objects[0]);
        });

        list.put("timeline.point.deleted", objects ->{
            model.removePoint((Point) objects[0]);
        });

        list.put("player.point.deleted", objects ->{
            model.removePoint((Point) objects[0]);
        });

        return list;
    }
}