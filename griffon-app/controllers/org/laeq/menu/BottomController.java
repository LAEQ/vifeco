package org.laeq.menu;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import java.util.*;

@ArtifactProviderFor(GriffonController.class)
public class BottomController extends AbstractGriffonController {
    @MVCMember @Nonnull private BottomModel model;
    @MVCMember @Nonnull private BottomView view;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }


    public void setMessage(String message, List<String> styles){
        runInsideUIAsync(() -> {
            view.setMessage(message, styles);
        });
    }

    private void setMessageParametized(Object[] objects, List<String> styles) {
        runInsideUIAsync(() -> {
            view.setMessageParametized(objects, styles);
        });
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        // Success
        list.put("status.success", objects -> {
            setMessage((String) objects[0], Arrays.asList("alert", "alert-success"));
        });
        list.put("status.success.parametrized", objects -> {
            setMessageParametized(objects, Arrays.asList("alert", "alert-success"));
        });

        //Error
        list.put("status.error", objects -> {
            setMessage((String) objects[0], Arrays.asList("alert", "alert-danger"));
        });
        list.put("status.error.parametrized", objects -> {
            setMessageParametized(objects, Arrays.asList("alert", "alert-danger"));
        });

        //Info
        list.put("status.info.parametrized", objects -> {
            setMessageParametized(objects, Arrays.asList("alert", "alert-info"));
        });
        list.put("status.info", objects -> {
            setMessage((String) objects[0], Arrays.asList("alert", "alert-info"));
        });

        //Info
        list.put("status.warning.parametrized", objects -> {
            setMessageParametized(objects, Arrays.asList("alert", "alert-warning"));
        });
        list.put("status.warning", objects -> {
            setMessage((String) objects[0], Arrays.asList("alert", "alert-warning"));
        });

        return list;
    }
}