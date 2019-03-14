package org.laeq.video.player;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.DAOException;
import org.laeq.graphic.icon.TimelineIcon;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconTimeline;
import org.laeq.service.MariaService;
import org.laeq.video.ControlsDefault;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ArtifactProviderFor(GriffonController.class)
public class TimelineController extends AbstractGriffonController {
    @MVCMember @Nonnull private TimelineModel model;
    @MVCMember @Nonnull private TimelineView view;
    @MVCMember @Nonnull private Video video;

    @Inject private MariaService mariaService;

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

        list.put("media.duration", objects -> {
            Duration duration = (Duration) objects[0];
            runInsideUISync(() -> {
                model.init(duration);
                view.init();
            });
        });

        list.put("player.play", objects -> view.play());

        list.put("point.added", objects -> runInsideUISync(() -> view.addPoint((Point) objects[0])));

        list.put("media.currentTime", objects -> runInsideUISync(() -> {
            view.updatePosition((Duration)objects[0]);
        }));

        list.put("controls.duration", objects -> runInsideUISync(() -> {
            view.updateDurationLine((double)objects[0]);
        }));

        list.put("controls.rate", objects -> view.updateRate((double)objects[0]));

        return list;
    }

    public void deletePoint(int identifier) {
        Optional<Point> point = video.getPointSet().stream().filter(p -> p.getId() == identifier).findFirst();

        if(point.isPresent()){
            try {
                mariaService.getPointDAO().delete(point.get());
                video.getPointSet().remove(point.get());
                getApplication().getEventRouter().publishEvent("point.deleted", Arrays.asList(point.get()));
            } catch (DAOException e) {
                getLog().error(e.getMessage());
            }
        }

    }

    public void highlightPoint(TimelineIcon intersectedNode) {
        getApplication().getEventRouter().publishEvent("point.hightlight", Arrays.asList(intersectedNode.getIdentifier()));
    }

    public void highlightPoint() {
        getApplication().getEventRouter().publishEvent("point.no_hightlight");
    }
}