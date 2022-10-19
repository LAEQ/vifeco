package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Video;
import javax.annotation.Nonnull;
import javafx.util.Duration;
import org.laeq.model.icon.IconPointColorized;

import java.util.Collection;


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

        container.setEventRouter(getApplication().getEventRouter());
    }

    @Override
    public void resizeListener() {
        iconAnchor.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            final double widthRatio = newValue.getWidth() / videoWidth;
            final double heightRatio = newValue.getHeight() / videoHeight;
            final double bestRatio = Math.min(widthRatio, heightRatio);
            final double width = videoWidth * bestRatio;
            final double height = videoHeight * bestRatio;

            model.setWidth(width);
            model.setHeight(height);

            container.setPrefWidth(width);
            container.setPrefHeight(height);
        });
    }

    public void updateCurrentTime(Duration currentTime) {
        System.out.println(currentTime);
        Collection<IconPointColorized> icons = model.setCurrentTime(currentTime);
        runInsideUIAsync(() -> {
            container.getChildren().clear();
            container.getChildren().addAll(icons);
        });
    }

    public Point2D getMousePosition() {
        return container.getMousePosition();
    }

    public void addIcon(IconPointColorized icon) {
        runInsideUIAsync(() -> {
            container.getChildren().add(icon);
        });
    }
}
