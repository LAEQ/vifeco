package org.laeq.video;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.user.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ControlsController extends AbstractGriffonController {
    @MVCMember @Nonnull private ControlsModel model;

    @Inject private PreferencesService prefService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        model.setPreferences(prefService.getPreferences());

        getApplication().getEventRouter().addEventListener(listenerList());
    }

    private Map<String, RunnableWithArgs> listenerList() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("rate.change", objects -> this.model.setRate((Double) objects[0]));

        return list;
    }


    public void changeRate(Number newValue) {
        model.getPrefs().rate = (Double) newValue;
        runOutsideUI(() -> prefService.export(model.getPrefs()));
        dispatchEvent("controls.rate", newValue);
    }

    public void changeVolume(Number newValue) {
        model.getPrefs().volume = (Double) newValue;
        runOutsideUI(() -> prefService.export(model.getPrefs()));
        dispatchEvent("controls.volume", newValue);
    }

    public void changeSize(Number newValue) {
        model.getPrefs().size = (Double) newValue;
        runOutsideUI(() -> prefService.export(model.getPrefs()));
        dispatchEvent("controls.size", newValue);
    }

    public void changeDuration(Number newValue) {
        model.getPrefs().duration = (Double) newValue;
        runOutsideUI(() -> prefService.export(model.getPrefs()));
        dispatchEvent("controls.duration", newValue);
    }

    public void changeOpacity(Number oldvalue, Number newValue) {
        model.getPrefs().opacity = (Double) newValue;
        runOutsideUI(() -> prefService.export(model.getPrefs()));
        dispatchEvent("controls.opacity", oldvalue, newValue);
    }

    private void dispatchEvent(String eventName, Number value){
        getApplication().getEventRouter().publishEventAsync(eventName, Arrays.asList(value));
    }

    private void dispatchEvent(String eventName, Number oldvalue, Number newvalue){
        getApplication().getEventRouter().publishEventAsync(eventName, Arrays.asList(oldvalue, newvalue));
    }
}