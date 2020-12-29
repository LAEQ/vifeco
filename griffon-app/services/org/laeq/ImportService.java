package org.laeq;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;

import java.io.File;
import java.io.IOException;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class ImportService extends AbstractGriffonService {

    public ImportService(){

    }

    public void execute(File file) throws IOException {

    }
}