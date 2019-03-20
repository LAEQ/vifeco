package org.laeq.video.player;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.DAOException;
import org.laeq.db.PointDAO;
import org.laeq.db.VideoDAO;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.service.MariaService;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class PlayerController extends AbstractGriffonController {
    @MVCMember @Nonnull private PlayerView view;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listenerList());
    }

    @Override
    public void mvcGroupDestroy() {

    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void play() {
        view.play();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void rewind() {
        view.backward(30);
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void forward() {
        view.forward(30);
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void backVideo() {

//        view.reload();
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void addPoint(Point point) {
        publishEvent("point.added", point);
    }

    private void publishEvent(String eventName, Object obj){
        getApplication().getEventRouter().publishEvent(eventName, Arrays.asList(obj));
    }

    private Map<String, RunnableWithArgs> listenerList(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

//        list.put("controls.rate", objects -> {
//            view.rate((Double) objects[0]);
//            model.setRate((Double) objects[0]);
//        });
//
//        list.put("controls.volume", objects -> {
//            Double value = (Double) objects[0];
//            view.volume(value);
//            model.setVolume(value);
//        });
//
//        list.put("controls.size", objects -> {
//            Double size = (Double) objects[0];
//            view.size(size);
//            model.setSize(size);
//        });
//
//        list.put("controls.opacity", objects -> {
//            view.opacity((Double)objects[0], (Double)objects[1]);
//            model.setOpacity((Double)objects[1]);
//        });
//
//        list.put("controls.duration", objects -> {
//            Double value = (Double) objects[0];
//            model.setDuration(value);
//            view.setDuration(value);
//        });

        return list;
    }

    private void dispatchEvent(String eventName){
        getApplication().getEventRouter().publishEvent(eventName);
    }

    private void dispatchEvent(String eventName, Object object){
        getApplication().getEventRouter().publishEvent(eventName, Arrays.asList(object));
    }


//    public void deletePoint(Point point) {
//        getApplication().getEventRouter().publishEvent("point.deleted", Arrays.asList(point));
//    }
}