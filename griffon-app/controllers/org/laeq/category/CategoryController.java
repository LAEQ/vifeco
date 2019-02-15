package org.laeq.category;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Category;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class CategoryController extends AbstractGriffonController {
    @MVCMember @Nonnull private CategoryModel model;
    @MVCMember @Nonnull private CategoryView view;

    private FileChooser fileChooser;

    @Inject private DialogService dialogService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }


    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void chooseFile(){
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpeg", "*.jpg", "*.gif")
        );

        Stage stage = (Stage) getApplication().getWindowManager().findWindow("mainWindow");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                model.iconProperty().set(selectedFile.getCanonicalFile().toURI().toString());
                view.generateIcon();
            } catch (IOException e) {
                getLog().error("CategoryController: file io exception " + e.getMessage());
            }

        } else {
            getLog().error("Error loading the file");
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void save(){
        boolean result = true;
        StringBuilder builder = new StringBuilder();

        if(model.getName().length() == 0){
            builder.append("You must provide a name\n");
            result = false;
        }

        if(model.getIcon().length() == 0){
            result = false;
            builder.append("You must provide a valid path to an icon\n");
        }

        if(model.getShortcut().length() != 1){
            result = false;
            builder.append("You must provide a unique one caractere shortcut");
        }

        if(result == false){
            dialogService.dialog(builder.toString());
        } else {
            Category category = new Category();
            category.setName(model.getName());
            category.setShortcut(model.getShortcut());
            category.setIcon(model.getShortcut());
            getApplication().getEventRouter().publishEventAsync("database.category.new", Arrays.asList(category));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void closeTab() {
        destroyMVCGroup(getMvcGroup().getMvcId());
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("category.create.failed", objects -> {
            dialogService.dialog("Failed to save new category " + ((Exception) objects[0]).getMessage());
        });


        return list;
    }
}