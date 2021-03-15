package org.laeq;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import java.util.LinkedHashMap;

@ArtifactProviderFor(GriffonModel.class)
public class ConfigModel extends AbstractGriffonModel {
    public LinkedHashMap<String, String> datas = new LinkedHashMap<>();
}