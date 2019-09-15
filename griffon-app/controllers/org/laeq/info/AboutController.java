package org.laeq.info;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.user.PreferencesService;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class AboutController extends AbstractGriffonController {
    @MVCMember @Nonnull private AboutModel model;
    @MVCMember @Nonnull private AboutView view;
    @Inject private PreferencesService preferenceService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();
        list.put("change.language", objects -> {
            Locale locale = (Locale) objects[0];
            view.changeLocale(locale);
        });

        return list;
    }
}