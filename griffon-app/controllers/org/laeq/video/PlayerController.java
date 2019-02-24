package org.laeq.video;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.scene.input.KeyEvent;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.DAOException;
import org.laeq.db.DatabaseService;
import org.laeq.db.PointDAO;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class PlayerController extends AbstractGriffonController {
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private PlayerView view;
    @MVCMember @Nonnull private Video video;
    @Inject private DialogService dialogService;
    @Inject private DatabaseService dbService;

    private PointDAO pointDAO;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listenerList());

        pointDAO = dbService.getPointDAO();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void play() {
        view.play();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void rewind() {
        view.backward();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void forward() {
        view.forward();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void backVideo() {
        view.reload();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void test(KeyEvent keyEvent) {
        System.out.println("test");
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void closeTab() {
        destroyMVCGroup(getMvcGroup().getMvcId());
    }

    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void addPoint(Point point, String letter) {
        try {
            Category category = model.debugCategory();
            point.setCategory(category);
            point.setVideo(video);
            pointDAO.insert(point);
            model.addPoint(point);

            view.addPoint(point);

            publishEvent("point.added", point);
        } catch (DAOException e) {
            getLog().error(String.format("PlayerCtrl: cannot save new point: %s : %s", point, e.getMessage()));
        }
    }

    private void publishEvent(String eventName, Object obj){
        getApplication().getEventRouter().publishEventAsync(eventName, Arrays.asList(obj));
    }

    private Map<String, RunnableWithArgs> listenerList(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("controls.rate", objects -> {
            view.rate((Double) objects[0]);
            model.setRate((Double) objects[0]);

        });

        list.put("controls.volume", objects -> {
            Double value = (Double) objects[0];
            view.volume(value);
            model.setVolume(value);
        });

        list.put("controls.size", objects -> {
            Double size = (Double) objects[0];
            view.size(size);
            model.setSize(size);
        });

        list.put("controls.opacity", objects -> {
            view.opacity((Double)objects[0], (Double)objects[1]);
            model.setOpacity((Double)objects[1]);
        });

        list.put("controls.duration", objects -> {
            Double value = (Double) objects[0];
            System.out.println("duration player: " + value);
            model.setDuration(value);
        });

        return list;
    }
}