package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.core.env.Metadata;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import javax.inject.Inject;
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

        Locale locale = getApplication().getLocale();

        view.setLocale(preferencesService.getLocaleIndex(getApplication().getLocale()));
    }

    private void fetchRelease(){
        try {
            String releaseUrl = metadata.get("release.api.url");
            String latestRelease = helperService.fetchLatestRelease(releaseUrl);


            if(latestRelease != null && metadata.getApplicationVersion().equals(latestRelease) == false){
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
    public void setLocale(Integer index){
        getApplication().setLocale(preferencesService.getLocale(index));
        getApplication().getEventRouter().publishEvent("config.section");
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void clear(){
        getApplication().getEventRouter().publishEvent("status.reset");
    }
}