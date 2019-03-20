package org.laeq.video.player;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.service.MariaService;
import org.laeq.video.ControlsDefault;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class TimelineController extends AbstractGriffonController {
    @MVCMember @Nonnull private TimelineModel model;
    @MVCMember @Nonnull private TimelineView view;
    @MVCMember @Nonnull private VideoEditor editor;


    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        model.setLineDuration(ControlsDefault.duration);
        getApplication().getEventRouter().addEventListener(listenerList());
    }

    @Override
    public void mvcGroupDestroy(){

    }

    private Map<String, RunnableWithArgs> listenerList(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

//        list.put("media.duration", objects -> {
//            Duration duration = (Duration) objects[0];
//            runInsideUISync(() -> {
//                model.init(duration);
//                view.init();
//            });
//        });
//
//        list.put("player.play", objects -> view.play());
//
//
//        list.put("media.currentTime", objects -> runInsideUISync(() -> {
//            view.updatePosition((Duration)objects[0]);
//        }));
//
//        list.put("controls.duration", objects -> runInsideUISync(() -> {
//            view.updateDurationLine((double)objects[0]);
//        }));
//
//        list.put("controls.rate", objects -> view.updateRate((double)objects[0]));

        return list;
    }

    public void deletePoint(Point point) {
        getApplication().getEventRouter().publishEvent("point.deleted", Arrays.asList(point));
    }
}