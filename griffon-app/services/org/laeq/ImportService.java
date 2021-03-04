package org.laeq;

import com.fasterxml.jackson.databind.ObjectMapper;
import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.Video;

import java.io.File;
import java.io.IOException;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class ImportService extends AbstractGriffonService {

    public ImportService(){

    }

    public void execute(File file) throws IOException {
        Video result = new ObjectMapper().readValue(file, Video.class);
        System.out.println(result);




    }
}