package org.laeq.info;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.env.Metadata;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.HelperService;
import org.laeq.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class AboutController extends AbstractGriffonController {
    @MVCMember @Nonnull private AboutModel model;
    @MVCMember @Nonnull private AboutView view;

    @Inject private PreferencesService preferencesService;
    @Inject private HelperService helperService;
    @Inject private Metadata metadata;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        fetchRelease();
    }

    private void fetchRelease(){
        try {
            String releaseUrl = metadata.get("release.url");
            String latestRelease = helperService.fetchLatestRelease(releaseUrl);

            if(metadata.getApplicationVersion().equals(latestRelease) == false){
                getApplication().getEventRouter().publishEventAsync("status.warning.parametrized", Arrays.asList("release.fetch.update", latestRelease));
            }

        } catch (Exception e) {
            getApplication().getEventRouter().publishEventAsync("status.error", Arrays.asList("release.fetch.error"));
        }
    }
}