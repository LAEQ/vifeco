package org.laeq.info;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Preferences;

@ArtifactProviderFor(GriffonModel.class)
public class AboutModel extends AbstractGriffonModel {

    private Preferences preferences;

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public Preferences getPreferences() {
        return preferences;
    }
}