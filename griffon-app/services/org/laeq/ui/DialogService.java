package org.laeq.ui;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.*;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;

import java.util.Optional;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class DialogService extends AbstractGriffonService {

    public void dialog(){
        ButtonType btn = new ButtonType("OK", ButtonData.OK_DONE);
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setContentText("Not implemented yet");
        dialog.getDialogPane().getButtonTypes().addAll(btn);
        boolean disabled = false;
        dialog.getDialogPane().lookupButton(btn).setDisable(disabled);

        Optional<ButtonType> result = dialog.showAndWait();
    }

    public void dialog(String text){
        ButtonType btn = new ButtonType("OK", ButtonData.OK_DONE);
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setContentText(text);
        dialog.getDialogPane().getButtonTypes().addAll(btn);
        boolean disabled = false;
        dialog.getDialogPane().lookupButton(btn).setDisable(disabled);

        Optional<ButtonType> result = dialog.showAndWait();
    }

    public Boolean confirm(String text) {
        ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setHeaderText("Confirmation");
        dialog.setContentText(text);
        dialog.getDialogPane().getButtonTypes().addAll(cancel, ok);

        Optional<ButtonType> result = dialog.showAndWait();


        if(result.isPresent() && result.get() == ok){
            return true;
        }

        return false;
    }

    public void simpleAlert(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid form");

        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

}