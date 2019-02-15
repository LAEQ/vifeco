package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.User;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class MenuController extends AbstractGriffonController {
    @MVCMember @Nonnull private MenuModel model;
    @MVCMember @Nonnull private MenuView view;

    private FileChooser fileChooser;

    @Inject private DialogService dialogService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }

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
            getApplication().getEventRouter().publishEventAsync("database.video.create", Arrays.asList(selectedFile));
        } else {
            System.out.println("Error loading the file");
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
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void preferences() {
        dialogService.dialog();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void quit() {
        getApplication().shutdown();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void newCategory() {
//        destroyMVCGroup(getMvcGroup().getMvcId());
//        dialogService.dialog();
        getApplication().getEventRouter().publishEvent("category.create");
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void listCategory() {
        dialogService.dialog();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void exportCategory() {
        dialogService.dialog();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void importCategory() {
        dialogService.dialog();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void exportDB() {
        dialogService.dialog();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void importDB() {
        dialogService.dialog();
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void backupDB() {
        dialogService.dialog();
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("menu.org.laeq.user.init", objects -> {
            List<User> userList = (List<User>) objects[0];
            userList.forEach(user -> {
                     view.getUserComboBox().getItems().add(user);
                     if(user.getIsActive()){
                         view.getUserComboBox().getSelectionModel().select(user);
                     }
                }
            );
        });

        list.put("user.created", objects -> {
           view.getUserComboBox().getItems().add((User) objects[0]);
        });

        return list;
    }


    public void setActiveUser(User selectedItem) {
        getApplication().getEventRouter().publishEventAsync("database.org.laeq.user.active", Arrays.asList(selectedItem));
    }
}