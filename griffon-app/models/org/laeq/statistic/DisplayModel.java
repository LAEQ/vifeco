package org.laeq.statistic;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.statistic.MatchedPoint;

@ArtifactProviderFor(GriffonModel.class)
public class DisplayModel extends AbstractGriffonModel {
    public MatchedPoint mp  = null;
}