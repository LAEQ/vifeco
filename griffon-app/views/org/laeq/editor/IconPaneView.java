package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Video;
import javax.annotation.Nonnull;


@ArtifactProviderFor(GriffonView.class)
public class IconPaneView extends AbstractJavaFXGriffonView implements PaneSizable {
    @MVCMember @Nonnull private IconPaneModel model;
    @MVCMember @Nonnull private PlayerView parentView;
    @MVCMember @Nonnull private Video video;

    @FXML private IconPane container;
    @FXML private AnchorPane iconAnchor;

    private int videoWidth = 960;
    private int videoHeight = 540;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.playerPane.getChildren().add(node);
        connectMessageSource(node);
        resizeListener();
    }

    @Override
    public void resizeListener() {
        iconAnchor.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            final double widthRatio = newValue.getWidth() / videoWidth;
            final double heightRatio = newValue.getHeight() / videoHeight;
            final double bestRatio = Math.min(widthRatio, heightRatio);

            container.setPrefWidth(videoWidth * bestRatio);
            container.setPrefHeight(videoHeight * bestRatio);
        });
    }
}
