package org.laeq;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import java.util.Arrays;
import java.util.List;

@ArtifactProviderFor(GriffonModel.class)
public class VifecoModel extends AbstractGriffonModel {
    public String currentGroup = "about";
    final public List<String> mvcKeep = Arrays.asList("vifeco", "menu", "bottom");
    final public List<String> windowKeep = Arrays.asList("mainWindow");
}