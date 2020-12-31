package org.laeq.video.player;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.PreferencesService;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Preferences;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class PlayerController extends AbstractGriffonController {
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private PlayerView view;
    @MVCMember @Nonnull private VideoEditor editor;

    @Inject private PreferencesService preferencesService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        Preferences preferences = preferencesService.getPreferences();
        model.setSize(preferences.size);
        model.setOpacity(preferences.opacity);
        model.setDuration(preferences.duration);
        model.setVolume(preferences.volume);
        model.setRate(preferences.rate);
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
        view.reload();
    }


    private void publishEvent(String eventName, Object obj){
        getApplication().getEventRouter().publishEvent(eventName, Arrays.asList(obj));
    }

    private Map<String, RunnableWithArgs> listenerList(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("controls.rate", objects -> {
            Double rate = (Double) objects[0];
            model.setRate(rate);
            view.rate(rate);
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
            model.setDuration(value);
            view.setDuration(value);
        });

        list.put("point.go_previous", objects -> {
            Category category = (Category) objects[0];
            editor.previousPoint(category);

        });

        list.put("point.go_next", objects -> {
            Category category = (Category) objects[0];
            editor.nextPoint(category);
        });

        return list;
    }

    private void dispatchEvent(String eventName){
        getApplication().getEventRouter().publishEvent(eventName);
    }

    private void dispatchEvent(String eventName, Object object){
        getApplication().getEventRouter().publishEvent(eventName, Arrays.asList(object));
    }

    public void deletePoint(Point point) {
        getApplication().getEventRouter().publishEvent("point.deleted", Arrays.asList(point));
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void addPoint(Point point) {
        publishEvent("point.added", point);
    }

    public void updateRate(double rate) {
        publishEvent("rate.change", rate);
    }
}