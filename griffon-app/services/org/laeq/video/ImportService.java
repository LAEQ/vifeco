package org.laeq.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.Video;

import java.io.IOException;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class ImportService extends AbstractGriffonService {

    public Video execute(String json) throws IOException {
        Video result = new ObjectMapper().readValue(json, Video.class);

        return result;
    }
}