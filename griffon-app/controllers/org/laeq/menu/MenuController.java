package org.laeq.menu;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.DatabaseService;
import org.laeq.ExportService;
import org.laeq.ImportService;
import org.laeq.PreferencesService;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.model.dao.VideoDAO;
import org.laeq.settings.Settings;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@ArtifactProviderFor(GriffonController.class)
public class MenuController extends AbstractGriffonController {
    private FileChooser fileChooser;
    private PreferencesService preferencesService;

    @Inject private ImportService importService;
    @Inject private DatabaseService dbService;
    @Inject private ExportService exportService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
        preferencesService = new PreferencesService();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void open() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(
                    "Video Files",
                    "*.mp4", "*.avi", "*mp3")
        );

        Stage stage = (Stage) getApplication().getWindowManager().findWindow("mainWindow");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("video.create.start"));

            runOutsideUIAsync(() -> {
                this.createVideo(selectedFile);
            });

        } else {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.create.error"));
        }
    }

    private void createVideo(File selectedFile) {
        try{
            Video video = new Video();
            String path = selectedFile.getAbsolutePath();
            User defaultUser = dbService.userDAO.findDefault();
            Collection defaultCollection = dbService.collectionDAO.findDefault();
            video.setPath(path);
            video.setCollection(defaultCollection);
            video.setUser(defaultUser);
            video.setDuration(Duration.UNKNOWN);
            if(dbService.videoDAO.create(video)){
                getApplication().getEventRouter().publishEvent("video.created");
                getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("video.create.success"));
            } else{
                getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.create.error"));
            }
        }catch (Exception e){
            getLog().error(e.getMessage());
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.create.error"));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public String archive() throws Exception {
        VideoDAO videoDAO = dbService.videoDAO;
        List<Video> videoList = videoDAO.findAll();
        List<String> srcFiles = new ArrayList<>();

        for(Video video : videoList){
            String fileName = exportService.export(video);
            srcFiles.add(fileName);
        }

        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy_MM_dd");
        String nowStr = dateTimeFormatter.format(now);

        String zipFileName = String.format("%s_%d.zip", nowStr, System.currentTimeMillis());
        String filePath = this.getPathExport(zipFileName);

        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (String srcFile : srcFiles) {
                File fileToZip = new File(srcFile);
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }

            zipOut.close();
            fos.close();

        } catch (IOException e ){
            getLog().error(e.getMessage());
        } finally {
            for(String srcFile : srcFiles){
                File file = new File(srcFile);
                file.delete();
            }

            return filePath;
        }
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("video.open", objects -> {
            open();
        });

        list.put("database.backup", objects -> {
            try {
                String filename = this.archive();
                getApplication().getEventRouter().publishEvent("status.info.parametrized", Arrays.asList("db.export.success", filename));
            } catch (Exception e) {
                getLog().error(e.getMessage());
                getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("db.export.error"));
            }
        });

        return list;
    }

    private String getPathExport(String filename){
        return String.format("%s/%s", Settings.exportPath, filename);
    }
}