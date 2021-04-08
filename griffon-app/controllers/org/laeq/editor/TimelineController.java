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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class TimelineController extends AbstractGriffonController {
    @MVCMember @Nonnull private TimelineModel model;
    @MVCMember @Nonnull private TimelineView view;
    @MVCMember @Nonnull private Video video;

    @Inject DatabaseService dbService;

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
            synchronized (this){
                model.points.add((Point) objects[0]);
                view.refesh();
            }
        });

        list.put("player.point.deleted", objects ->{
            synchronized (this){
                model.points.remove(objects[0]);
                view.refesh();
            }
        });

        return list;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void updateCurrentTime(Duration start) {
        getApplication().getEventRouter().publishEventAsync("row.currentTime", Arrays.asList(start));
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void deletePoint(Point point) {
        runInsideUIAsync(() -> {
            if(dbService.pointDAO.delete(point)){
                video.getPoints().remove(point);
                getApplication().getEventRouter().publishEvent("timeline.point.deleted", Arrays.asList(point));

                synchronized (this){
                    view.clear();
                    model.points.remove(point);
                    view.refesh();
                }
            }
        });
    }
}