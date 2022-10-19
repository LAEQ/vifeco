package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Icon;
import org.laeq.model.icon.IconSVG;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class PlayerControlsView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private PlayerControlsController controller;
    @MVCMember @Nonnull private PlayerControlsModel model;

    @MVCMember @Nonnull private PlayerView parentView;

    @FXML private Button playActionTarget;
    @FXML private Button stopActionTarget;
    @FXML private Button rewindActionTarget;
    @FXML private Button forwardActionTarget;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.playerControls.getChildren().add(node);
        connectActions(node, controller);
        connectMessageSource(node);

        Icon icon = new Icon(IconSVG.btnPlay, org.laeq.model.icon.Color.white);
        playActionTarget.setGraphic(icon);
        playActionTarget.setText("");

        icon = new Icon(IconSVG.btnPause, org.laeq.model.icon.Color.white);
        stopActionTarget.setGraphic(icon);
        stopActionTarget.setText("");

        icon = new Icon(IconSVG.backward30, org.laeq.model.icon.Color.gray_dark);
        rewindActionTarget.setGraphic(icon);
        rewindActionTarget.setText("");

        icon = new Icon(IconSVG.forward30, org.laeq.model.icon.Color.gray_dark);
        forwardActionTarget.setGraphic(icon);
        forwardActionTarget.setText("");
    }
}
