package org.laeq.editor;

import griffon.core.Observable;
import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Drawing;
import org.laeq.model.Video;
import javax.annotation.Nonnull;
import javafx.util.Duration;
import org.laeq.model.icon.IconPointColorized;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ArtifactProviderFor(GriffonView.class)
public class IconPaneView extends AbstractJavaFXGriffonView implements PaneSizable {
    @MVCMember @Nonnull private IconPaneModel model;
    @MVCMember @Nonnull private IconPaneController controller;
    @MVCMember @Nonnull private EditorView parentView;

    @MVCMember @Nonnull private Video video;

    @FXML private IconPane container;
    @FXML private AnchorPane iconAnchor;

    @FXML private DrawingPane drawingPane;

    private int videoWidth = 960;
    private int videoHeight = 540;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.playerPane.getChildren().add(node);
        connectMessageSource(node);

        resizeListener();

        container.setEventRouter(getApplication().getEventRouter());

        this.updateCurrentTime(Duration.ZERO);
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
            drawingPane.setPrefWidth(width);
            drawingPane.setPrefHeight(height);

            refresh();
        });
    }

    public void updateCurrentTime(Duration currentTime) {
        Collection<IconPointColorized> icons = model.setCurrentTime(currentTime);
        runInsideUIAsync(() -> {
            List<Node> out = container.getChildren().stream().filter(node -> icons.contains(node) == false).collect(Collectors.toList());
            container.getChildren().removeAll(out);
            icons.forEach(iconPointColorized -> {
                if(container.getChildren().contains(iconPointColorized) == false){
                    container.getChildren().add(iconPointColorized);
                }
            });
        });
    }

    public Point2D getMousePosition() {
        return container.getMousePosition();
    }

    public void addIcon(IconPointColorized icon) {
        runInsideUIAsync(() -> container.getChildren().add(icon));
    }

    public void refreshOpacity(Double opacity) {
        runInsideUISync(() -> container.getChildren().forEach(node -> node.setOpacity(opacity)));
    }
    public void refreshSize(Double size) {
        runInsideUISync(() -> container.getChildren().forEach(node -> {
            node.setScaleX(size / 100);
            node.setScaleY(size / 100);
        }));
    }

    public void drawLineStart(String color) {
        container.startDrawLine(color);
    }

    public void drawRectangleStart(String color) {
        container.drawRectangle(color);
    }

    public void deleteDraw(Drawing drawing) {
        drawingPane.drawings.remove(drawing);
    }

    public void showDraw(Drawing drawing) {
        runInsideUISync(() -> {
            drawingPane.drawings.add(drawing);
        });
    }

    public void hideDraw(Drawing drawing) {
        runInsideUISync(() -> {
            drawingPane.drawings.remove(drawing);
        });
    }

    public void drawingDestroy() {
        runInsideUISync(() -> drawingPane.drawings.clear());
    }

    public void drawingUpdated(List<Drawing> list) {
        runInsideUISync(() -> {
            drawingPane.drawings.clear();
            drawingPane.drawings.addAll(list);
        });
    }

    public void refresh() {
        container.getChildren().clear();
        Collection<IconPointColorized> icons = model.setCurrentTime(model.getCurrentTime());
        container.getChildren().addAll(icons);
    }

    public void removeIcon(IconPointColorized icon) {
        runInsideUISync(() -> {
            container.getChildren().remove(icon);
        });

    }
}
