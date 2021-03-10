package org.laeq.tools;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.DatabaseService;
import org.laeq.ImportService;
import org.laeq.PreferencesService;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.util.*;

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
            model.collections.addAll(dbService.collectionDAO.findAll());
            model.users.addAll(dbService.userDAO.findAll());
            getApplication().getEventRouter().addEventListener(listeners());
            getApplication().getEventRouter().publishEventAsync("status.reset");
        } catch (Exception e) {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.collection.fetch.error"));
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
                model.video = null;
            }
        }
    }


    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void clear() {
        model.clear();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void confirm() {
        try {
            if(model.valid.get()){
                dbService.videoDAO.create(model.video);
                getApplication().getEventRouter().publishEvent("video.import.success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.import.error"));
        } finally {
            model.clear();
        }
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();


        return list;
    }
}