package org.laeq.menu;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Preferences;

@ArtifactProviderFor(GriffonModel.class)
public class MenuModel extends AbstractGriffonModel {
    private Preferences prefs;

    public Preferences getPrefs() {
        return prefs;
    }

    public void setPrefs(Preferences prefs) {
        this.prefs = prefs;
    }
}