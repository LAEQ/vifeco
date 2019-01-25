package org.laeq.ui;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;

import java.util.Optional;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class DialogService extends AbstractGriffonService {

    public void dialog(){
        ButtonType btn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setContentText("Not implemented yet");
        dialog.getDialogPane().getButtonTypes().addAll(btn);
        boolean disabled = false;
        dialog.getDialogPane().lookupButton(btn).setDisable(disabled);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            formatSystem();
        }

    }

    private void formatSystem() {
        System.out.println("Format system");
    }

}