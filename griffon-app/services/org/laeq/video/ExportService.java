package org.laeq.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.Category;
import org.laeq.model.Video;
import org.laeq.model.serializer.VideoSerializer;
import org.laeq.service.statistic.StatisticService;
import org.laeq.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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

    public String export(Video video){
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        Date now = new Date(System.currentTimeMillis());

        try {
            String fileName = String.format("%s-%s.json", video.getName(), formatter.format(now));
            fileName = getPathExport(fileName);
            objectMapper.writeValue(new File(fileName), video);

            return fileName;
        } catch (Exception e) {
            getLog().error(e.getMessage());
        }

        return "";
    }





    public void export(StatisticService service){
        ObjectMapper objectMapper = new ObjectMapper();
        Long now = System.currentTimeMillis();

        try{
            String statFileName = String.format("%s%s%s-%s.json", Settings.statisticPath, File.separator, service.getVideo1().getName(), System.currentTimeMillis());

            ObjectMapper mapper = new ObjectMapper();

            SimpleModule module = new SimpleModule();
            module.addSerializer(Video.class, new VideoSerializer());
            mapper.registerModule(module);

            mapper.writeValue(new File(statFileName), service);

        } catch (Exception exception){
            getLog().error(exception.getMessage());
        }
    }

    private String getPathExport(String filename){
        return String.format("%s/%s", Settings.exportPath, filename);
    }
}