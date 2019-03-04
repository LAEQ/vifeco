package org.laeq.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.Video;
import org.laeq.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class ExportService extends AbstractGriffonService {
    public ExportService(){
        String exportFolder = Settings.exportPath;

        Path path = Paths.get(exportFolder);

        if(!Files.exists(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                getLog().error(e.getMessage());
            }
        }
    }

    public void export(Video video){
        ObjectMapper objectMapper = new ObjectMapper();
        Long now = System.currentTimeMillis();

        try {
            String fileName = String.format("%s-%d.json", video.getName(), now);
            fileName = getPathExport(fileName);
            objectMapper.writeValue(new File(fileName), video);
        } catch (Exception e) {
            getLog().error(e.getMessage());
        }
    }

    private String getPathExport(String filename){
        return String.format("%s/%s", Settings.exportPath, filename);
    }
}