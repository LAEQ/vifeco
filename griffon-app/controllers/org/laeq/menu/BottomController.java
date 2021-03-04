package org.laeq.menu;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.i18n.MessageSource;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class BottomController extends AbstractGriffonController {
    private BottomModel model;

    private MessageSource messageSource;

    @MVCMember
    public void setModel(@Nonnull BottomModel model){
        this.model = model;
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());

        messageSource = getApplication().getMessageSource();
    }

    public void setMessage(String message, List<String> styles){
        String text = messageSource.getMessage(message);

        runInsideUISync(() -> {
            model.message.set(text);
            model.styles.clear();
            model.styles.addAll(styles);
        });
    }

    private void setMessageParametized(Object[] objects, List<String> styles) {
        String key = (String) objects[0];
        String param = (String) objects[1];
        String text = messageSource.getMessage(key, Arrays.asList(param));

        runInsideUISync(() -> {
            model.message.set(text);
            model.styles.clear();
            model.styles.addAll(styles);
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