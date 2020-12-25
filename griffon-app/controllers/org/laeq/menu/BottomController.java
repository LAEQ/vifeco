package org.laeq.menu;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class BottomController extends AbstractGriffonController {
    @MVCMember @Nonnull private BottomModel model;
    @MVCMember @Nonnull private BottomView view;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }


    public void setSuccess(String message){
        runInsideUIAsync(() -> {
            view.setSuccessMessage(message);
        });

    }

    public void setError(String message){
        runInsideUIAsync(() -> {
            view.setErrorMessage(message);
        });

    }

    public void setInfo(String message){
       runInsideUIAsync(() -> {
           view.setInfo(message);
       });
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();
        list.put("status.success", objects -> {
            setSuccess((String) objects[0]);
        });

        list.put("status.info.parametized", objects -> {
            setInfoParametized(objects);
        });

        list.put("status.error", objects -> {
            String message = (String) objects[0];
            setError(message);
        });

        list.put("status.info", objects -> {
            String message = (String) objects[0];
            setInfo(message);
        });

        return list;
    }

    private void setInfoParametized(Object[] objects) {
        view.setInfoParametized(objects);
    }
}