package org.laeq.video;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ControlsController extends AbstractGriffonController {
    @MVCMember @Nonnull private ControlsModel model;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listenerList());
    }

    private Map<String, RunnableWithArgs> listenerList() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("rate.change", objects -> this.model.setRate((Double) objects[0]));

        return list;
    }


    public void changeRate(Number newValue) {
        dispatchEvent("controls.rate", newValue);
    }

    public void changeVolume(Number newValue) {
        dispatchEvent("controls.volume", newValue);
    }

    public void changeSize(Number newValue) {
        dispatchEvent("controls.size", newValue);
    }

    public void changeDuration(Number newValue) {
        dispatchEvent("controls.duration", newValue);
    }

    public void changeOpacity(Number oldvalue, Number newValue) {
        dispatchEvent("controls.opacity", oldvalue, newValue);
    }

    private void dispatchEvent(String eventName, Number value){
        getApplication().getEventRouter().publishEventAsync(eventName, Arrays.asList(value));
    }

    private void dispatchEvent(String eventName, Number oldvalue, Number newvalue){
        getApplication().getEventRouter().publishEventAsync(eventName, Arrays.asList(oldvalue, newvalue));
    }
}