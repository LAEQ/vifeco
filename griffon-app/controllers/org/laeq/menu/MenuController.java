package org.laeq.menu;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.DatabaseService;
import org.laeq.TranslationService;
import org.laeq.db.*;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.settings.Settings;
import org.laeq.ui.DialogService;
import org.laeq.video.ExportService;
import org.laeq.video.ImportService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.*;

@ArtifactProviderFor(GriffonController.class)
public class MenuController extends AbstractGriffonController {
    private Locale currentLocale;
    @MVCMember @Nonnull private MenuModel model;
    @MVCMember @Nonnull private MenuView view;

    private FileChooser fileChooser;
    private TranslationService translationService;

    @Inject private DialogService dialogService;
    @Inject private ImportService importService;
    @Inject private DatabaseService dbService;
    @Inject private ExportService exportService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        this.setTranslationService();
        getApplication().getEventRouter().addEventListener(listeners());
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void open() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(
                    "Video Files",
                    "*.mp4", "*.wav", "*.mkv", "*.avi", "*.wmv", "*.mov")
        );

        Stage stage = (Stage) getApplication().getWindowManager().findWindow("mainWindow");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("video.create.start"));

            this.createVideo(selectedFile);

        } else {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.create.error"));
        }
    }

    private void createVideo(File selectedFile) {
        try {
            Video video = new Video();
            String path = selectedFile.getAbsolutePath();
            User defaultUser = dbService.userDAO.findDefault();
            Collection defaultCollection = dbService.collectionDAO.findDefault();
            video.setPath(path);
            video.setCollection(defaultCollection);
            video.setUser(defaultUser);

            Media media = new Media(selectedFile.getCanonicalFile().toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);


            System.out.println("Video before addition");

            mediaPlayer.setOnError(() -> {
                System.out.println(mediaPlayer.getError());
                getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.create.error"));
            });

            mediaPlayer.setOnReady(()-> {
                video.setDuration(mediaPlayer.getTotalDuration());
                try {
                    dbService.videoDAO.create(video);
                    getApplication().getEventRouter().publishEvent("status.success", Arrays.asList("video.create.success"));
                    getApplication().getEventRouter().publishEvent("video.created", Arrays.asList(video));

                } catch (Exception e) {
                    e.printStackTrace();
                    getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.create.error"));
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.create.error"));
        }

    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void importVideo() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Import video file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Video Files",
                        "*.json")
        );

        Stage stage = (Stage) getApplication().getWindowManager().findWindow("mainWindow");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {

            try {
                importService.execute(selectedFile);
                getApplication().getEventRouter().publishEvent("video.import.success");
            } catch (IOException | DAOException e) {
                dialogService.simpleAlert(
                        getApplication().getMessageSource().getMessage("org.laeq.title.error"),
                        getApplication().getMessageSource().getMessage("org.laeq.video.import.error")
                );
            }
        } else {
            getLog().error("Error loading the file");
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void close(){
        dialogService.dialog();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void save(){
        dialogService.dialog();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void saveAs(){
        dialogService.dialog();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void sendTo(){
        getApplication().getEventRouter().publishEvent("org.laeq.user.create");
        getApplication().getEventRouter().publishEvent("org.laeq.user.list");
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public String archive() throws IOException {
//        VideoDAO videoDAO = dbService.getVideoDAO();
//        CategoryDAO categoryDAO = dbService.getCategoryDAO();
//        UserDAO userDAO = dbService.getUserDAO();
//        PointDAO pointDAO = dbService.getPointDAO();
//
//        List<Video> videoList = videoDAO.findAll();
//
//        List<String> srcFiles = new ArrayList<>();
//
//        for(Video video : videoList){
//            String fileName = exportService.export(video);
//            srcFiles.add(fileName);
//        }
//
        String zipFileName = String.format("%s.zip", System.currentTimeMillis());
//
//        try {
//            FileOutputStream fos = new FileOutputStream(getPathExport(zipFileName));
//            ZipOutputStream zipOut = new ZipOutputStream(fos);
//            for (String srcFile : srcFiles) {
//                File fileToZip = new File(srcFile);
//                FileInputStream fis = new FileInputStream(fileToZip);
//                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
//                zipOut.putNextEntry(zipEntry);
//
//                byte[] bytes = new byte[1024];
//                int length;
//                while ((length = fis.read(bytes)) >= 0) {
//                    zipOut.write(bytes, 0, length);
//                }
//                fis.close();
//            }
//
//            zipOut.close();
//            fos.close();
//
//        } catch (IOException e ){
//            getLog().error(e.getMessage());
//        } finally {
//            for(String srcFile : srcFiles){
//                File file = new File(srcFile);
//                file.delete();
//            }
//
            return zipFileName;
//        }
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("video.open", objects -> {
            open();
        });

        list.put("video.import", objects -> {
            importVideo();
        });

        list.put("database.backup", objects -> {
            try {
                String path = this.getPathExport(this.archive());
                getApplication().getEventRouter().publishEvent("status.info.parametrized", Arrays.asList("db.export.success", path));
            } catch (IOException e) {
                getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("db.export.error"));
            }
        });

        return list;
    }

    public void changeLanguage() {
        getApplication().getEventRouter().publishEvent("change.language", Arrays.asList(model.getPrefs().locale));
    }

    private String getPathExport(String filename){
        return String.format("%s/%s", Settings.exportPath, filename);
    }

    private void alert(String key, String alertMsg){
        runInsideUISync(() -> dialogService.simpleAlert(key, alertMsg));
    }

    private void setTranslationService(){
        try {
            translationService = new TranslationService(getClass().getClassLoader().getResourceAsStream("messages/messages.json"), model.getPrefs().locale);
        } catch (IOException e) {
            getLog().error("Cannot load file messages.json");
        }
    }
}