package org.laeq.tools;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
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
public class ImportController extends AbstractGriffonController {
    @MVCMember @Nonnull private ImportModel model;

    private FileChooser fileChooser;
    private PreferencesService preferencesService;

    @Inject private ImportService importService;
    @Inject private DatabaseService dbService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try {
            model.collectionList.addAll(dbService.collectionDAO.findAll());
            getApplication().getEventRouter().addEventListener(listeners());
        } catch (Exception e) {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.collection.fetch.error"));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void open() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(
                    "Json Files",
                    "*.json")
        );

        Stage stage = (Stage) getApplication().getWindowManager().findWindow("mainWindow");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            System.out.println("Importing " + selectedFile.getAbsolutePath());

        } else {
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
                Video video = importService.execute(selectedFile);

                model.setVideo(video);

            } catch (Exception e) {
                e.printStackTrace();
                getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.import.error"));
            }
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void confirm() {
        if(model.video != null){


            getApplication().getEventRouter().publishEvent("video.import.success");
        } else {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.import.error"));
        }
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();


        return list;
    }
}