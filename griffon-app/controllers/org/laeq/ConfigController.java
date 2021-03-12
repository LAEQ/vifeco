package org.laeq;

import griffon.core.GriffonApplication;
import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.core.env.Metadata;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.User;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ConfigController extends AbstractGriffonController{
    @MVCMember @Nonnull private ConfigModel model;
    @MVCMember @Nonnull private ConfigView view;

    @Inject private PreferencesService preferencesService;
    @Inject private HelperService helperService;
    @Inject private Metadata metadata;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
        fetchRelease();
    }

    private void fetchRelease(){
        try {
            String releaseUrl = metadata.get("release.url");
            String latestRelease = helperService.fetchLatestRelease(releaseUrl);
            model.latestVersion.bindBidirectional(new ReadOnlyStringWrapper(latestRelease));

            if(metadata.getApplicationVersion().equals(latestRelease) == false){
//                getApplication().getEventRouter().publishEventOutsideUI("status.info", Arrays.asList("db.category.fetch.success"));
                getApplication().getEventRouter().publishEventAsync("status.warning.parametrized",Arrays.asList("release.fetch.update", latestRelease));
            }

        } catch (Exception e) {
            getApplication().getEventRouter().publishEventAsync("status.error", Arrays.asList("release.fetch.error"));
        }
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void setLocale(String key){
        getApplication().setLocale(Locale.FRENCH);
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void clear(){
        getApplication().getEventRouter().publishEvent("status.reset");
    }
}