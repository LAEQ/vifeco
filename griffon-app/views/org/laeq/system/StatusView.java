package org.laeq.system;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.template.MiddlePaneView;

import java.util.Collections;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class StatusView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private StatusController controller;
    @MVCMember @Nonnull private StatusModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private Text connectionStatus;
    @FXML private Text tableStatus;
    @FXML private Text userStatus;
    @FXML private Text collectionStatus;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        connectActions(node, controller);

        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);

        connectionStatus.textProperty().bind(model.connectionStatusProperty());
        tableStatus.textProperty().bind(model.tableStatusProperty());
        userStatus.textProperty().bind(model.userStatusProperty());
        collectionStatus.textProperty().bind(model.collectionStatusProperty());
    }

    @Override
    public void mvcGroupDestroy(){

    }
}
