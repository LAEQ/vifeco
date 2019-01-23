package org.laeq;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;

@ArtifactProviderFor(GriffonController.class)
public class MenuController extends AbstractGriffonController {
    private MenuModel model;

    private FileChooser fileChooser;

    @MVCMember
    public void setModel(@Nonnull MenuModel model) {
        this.model = model;
    }

    @MVCMember @Nonnull
    private MenuView view;

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void open() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.wav", "*.mkv", "*.avi")
                );

        Stage stage = (Stage) getApplication().getWindowManager().findWindow("mainWindow");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            getApplication().getEventRouter().publishEventAsync("menu.open.video", Arrays.asList(selectedFile));
        } else {
            System.out.println("Error loading the file");
        }

    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void quit() {

    }
}