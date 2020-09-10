package org.laeq.menu;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.TranslationService;
import org.laeq.db.*;
import org.laeq.model.Category;
import org.laeq.model.Video;
import org.laeq.service.MariaService;
import org.laeq.settings.Settings;
import org.laeq.ui.DialogService;
import org.laeq.video.ExportService;
import org.laeq.video.ImportService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@ArtifactProviderFor(GriffonController.class)
public class MenuController extends AbstractGriffonController {
    private Locale currentLocale;
    @MVCMember @Nonnull private MenuModel model;
    @MVCMember @Nonnull private MenuView view;

    private FileChooser fileChooser;
    private TranslationService translationService;

    @Inject private DialogService dialogService;
    @Inject private ImportService importService;
    @Inject private MariaService dbService;
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
            getApplication().getEventRouter().publishEvent("video.create", Arrays.asList(selectedFile));
        } else {
            System.out.println("Error loading the file");
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
    public void archive() throws IOException {
        VideoDAO videoDAO = dbService.getVideoDAO();
        CategoryDAO categoryDAO = dbService.getCategoryDAO();
        UserDAO userDAO = dbService.getUserDAO();
        PointDAO pointDAO = dbService.getPointDAO();

        List<Video> videoList = videoDAO.findAll();

        List<String> srcFiles = new ArrayList<>();

        for(Video video : videoList){
            Set<Category> categories = categoryDAO.findByCollection(video.getCollection());
            video.getCollection().getCategorySet().addAll(categories);

            video.getPointSet().addAll(pointDAO.findByVideo(video));

            String fileName = exportService.export(video);
            srcFiles.add(fileName);
        }

        String userDefault = "";
        try {
            userDefault = userDAO.findDefault().toString();
        } catch (Exception e) {
            userDefault = UUID.randomUUID().toString().substring(0, 8);
        }

        String zipFileName = String.format("%s-%s.zip", userDefault, System.currentTimeMillis());

        try {
            FileOutputStream fos = new FileOutputStream(getPathExport(zipFileName));
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
        }
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
            String title = this.translationService.getMessage("org.laeq.title.success");
            String message = this.translationService.getMessage("org.laeq.video.export.success");

            try {
                this.archive();
            } catch (IOException e) {
                message = this.translationService.getMessage("org.laeq.video.export.error");
            } finally {
                alert(title, message);
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