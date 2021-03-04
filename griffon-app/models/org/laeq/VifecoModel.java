package org.laeq;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

@ArtifactProviderFor(GriffonModel.class)
public class VifecoModel extends AbstractGriffonModel {
    public String currentGroup = "statistic";
}