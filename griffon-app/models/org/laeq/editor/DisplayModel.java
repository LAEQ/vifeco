package org.laeq.editor;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

@ArtifactProviderFor(GriffonModel.class)
public class DisplayModel extends AbstractGriffonModel {
    public SimpleBooleanProperty volume = new SimpleBooleanProperty(Boolean.FALSE);
}