package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class BottomView extends AbstractJavaFXGriffonView {
    private BottomController controller;
    private BottomModel model;

    @MVCMember @Nonnull
    private VifecoView parentView;

    @FXML
    private Label clickLabel;

    @MVCMember
    public void setController(@Nonnull BottomController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull BottomModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();

        connectActions(node, controller);

        parentView.getBottom().getChildren().add(node);
    }
}
