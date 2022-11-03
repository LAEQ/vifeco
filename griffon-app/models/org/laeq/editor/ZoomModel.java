package org.laeq.editor;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Drawing;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ArtifactProviderFor(GriffonModel.class)
public class ZoomModel extends AbstractGriffonModel {

    @Inject private ZoomController controller;
    @MVCMember @Nonnull private Double mediaWidth = 0d;
    @MVCMember @Nonnull private Double mediaHeight = 0d;

    public double minZoom = 1d;
    public double maxZoom = 4d;
    public double bgWidth = 0;
    public double bgHeight = 0;

    public double frameWidthInit = 330.0;
    public double frameHeightInit = 200.0;

    public SimpleDoubleProperty frameX = new SimpleDoubleProperty(0);
    public SimpleDoubleProperty frameY = new SimpleDoubleProperty(0);

    public SimpleDoubleProperty translateX = new SimpleDoubleProperty(0);

    public SimpleDoubleProperty translateY = new SimpleDoubleProperty(0);

    public SimpleDoubleProperty frameWidth = new SimpleDoubleProperty(330.0);
    public SimpleDoubleProperty frameHeight = new SimpleDoubleProperty(200.0);

    private SimpleDoubleProperty zoom = new SimpleDoubleProperty(1);
    private Point2D start;

    public ZoomModel(){
        zoomProperty().addListener((observable, oldValue, newValue) -> {
            final double zoomFactor = newValue.doubleValue();
            double width = this.frameWidthInit / zoomFactor;
            double height = this.frameHeightInit / zoomFactor;

            this.setFrameWidth(width);
//            this.setFrameHeight(height);
            this.setFrameX(1);

//            controller.setZoomProperty();
        });
    }


    public double getZoom() {
        return zoom.get();
    }

    public SimpleDoubleProperty zoomProperty() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom.set(zoom);
    }

    @Nonnull
    public Double getMediaWidth() {
        return mediaWidth;
    }

    public void setMediaWidth(@Nonnull Double mediaWidth) {
        this.mediaWidth = mediaWidth;
    }

    @Nonnull
    public Double getMediaHeight() {
        return mediaHeight;
    }

    public void setMediaHeight(@Nonnull Double mediaHeight) {
        this.mediaHeight = mediaHeight;
    }

    public double getFrameWidth() {
        return frameWidth.get();
    }

    public SimpleDoubleProperty frameWidthProperty() {
        return frameWidth;
    }

    public void setFrameWidth(double frameWidth) {
        this.frameWidth.set(frameWidth);
    }

    public double getFrameX() {
        return frameX.get();
    }

    public SimpleDoubleProperty frameXProperty() {
        return frameX;
    }

    public void setFrameX(double frameX) {
        this.frameX.set(frameX);

        if(this.getFrameX() < 0){
            this.setFrameX(0);
        }
    }

    public double getFrameY() {
        return frameY.get();
    }

    public SimpleDoubleProperty frameYProperty() {
        return frameY;
    }

    public void setFrameY(double frameY) {
        this.frameY.set(frameY);
    }

    public double getFrameHeight() {
        return frameHeight.get();
    }

    public SimpleDoubleProperty frameHeightProperty() {
        return frameHeight;
    }

    public void setFrameHeight(double frameHeight) {
        this.frameHeight.set(frameHeight);
    }

    public void setStart(Point2D start) {
        this.start = start;
        
    }

    public Point2D getStart() {
        return this.start;
    }
}
