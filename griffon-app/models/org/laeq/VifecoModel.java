package org.laeq;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonModel.class)
public class VifecoModel extends AbstractGriffonModel {
    public String currentGroup = "statistic";
}