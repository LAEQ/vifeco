package org.laeq.video.player;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Point;
import org.laeq.video.ControlsDefault;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class TimelineController extends AbstractGriffonController {
    @MVCMember @Nonnull private TimelineModel model;
    @MVCMember @Nonnull private TimelineView view;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        model.setLineDuration(ControlsDefault.duration);
        getApplication().getEventRouter().addEventListener(listenerList());
    }

    @Override
    public void mvcGroupDestroy(){
        view.destroy();
    }

    private Map<String, RunnableWithArgs> listenerList(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("controls.duration", objects -> runInsideUISync(() -> {
            view.updateDurationLine((double)objects[0]);
        }));

        return list;
    }

    public void deletePoint(Point point) {
        getApplication().getEventRouter().publishEvent("point.deleted", Arrays.asList(point));
    }
}