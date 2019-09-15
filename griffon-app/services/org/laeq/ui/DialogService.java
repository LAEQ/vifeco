package org.laeq.ui;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.text.Text;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;

import java.util.Optional;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class DialogService extends AbstractGriffonService {

    public void dialog(){
        runInsideUISync(() -> {
            ButtonType btn = new ButtonType("OK", ButtonData.OK_DONE);
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);

            dialog.setTitle("Not implemented");
            dialog.getDialogPane().getButtonTypes().addAll(btn);
            boolean disabled = false;
            dialog.getDialogPane().lookupButton(btn).setDisable(disabled);

            dialog.show();
        });
    }

    public void error(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        Text text = new Text(message);
        text.setWrappingWidth(300);
        alert.getDialogPane().setContent(text);
        alert.getDialogPane().setPadding(new Insets(10, 10,10,5));
        alert.show();
    }

    public void dialog(String text){
        ButtonType btn = new ButtonType("OK", ButtonData.OK_DONE);
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setContentText(text);Text textBox = new Text(text);
        textBox.setWrappingWidth(200);
        dialog.getDialogPane().setContent(textBox);
        dialog.getDialogPane().setPadding(new Insets(20,10,10,10));

        dialog.getDialogPane().getButtonTypes().addAll(btn);
        boolean disabled = false;
        dialog.getDialogPane().lookupButton(btn).setDisable(disabled);

        Optional<ButtonType> result = dialog.showAndWait();
    }

    public Boolean confirm(String text) {
        ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setHeaderText(null);
        Text textBox = new Text(text);
        dialog.getDialogPane().setPadding(new Insets(5,10,5,10));
        textBox.setWrappingWidth(200);
        dialog.getDialogPane().setContent(textBox);
        dialog.getDialogPane().getButtonTypes().addAll(cancel, ok);

        Optional<ButtonType> result = dialog.showAndWait();


        if(result.isPresent() && result.get() == ok){
            return true;
        }

        return false;
    }

    public void simpleAlert(String title, String text){
        runInsideUISync(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setWidth(250);

            alert.setHeaderText(null);
            Text textBox = new Text(text);
            textBox.setWrappingWidth(300);

            alert.getDialogPane().setContent(textBox);
            alert.getDialogPane().setPadding(new Insets(5,10,10,5));
            alert.showAndWait();
        });
    }

}