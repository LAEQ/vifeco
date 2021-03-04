package org.laeq.menu;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class BottomView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private BottomController controller;
    @MVCMember @Nonnull private BottomModel model;

    @MVCMember @Nonnull private VifecoView parentView;

    @FXML public Label message;
    @FXML private Pane box;

    @Override
    public void initUI() {
        Node node = loadFromFXML();

        connectActions(node, controller);

        model.message.bindBidirectional(message.textProperty());

        model.styles.addListener((ListChangeListener<String>) c -> {
            message.getStyleClass().clear();
            message.getStyleClass().setAll(c.getList());
        });

        parentView.bottom.getChildren().add(node);
    }
}
