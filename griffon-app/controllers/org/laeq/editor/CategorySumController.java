package org.laeq.editor;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.DatabaseService;
import org.laeq.model.Point;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import javax.inject.Inject;
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

        list.put("point.removed", objects ->{
            model.removePoint((Point) objects[0]);
        });

        return list;
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD_ASYNC)
    public void updateCurrentTime(Duration start) {
        System.out.println("update current time " + start);
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD_ASYNC)
    public void deletePoint(Point point) {
        System.out.println("update current time " + point);
    }
}