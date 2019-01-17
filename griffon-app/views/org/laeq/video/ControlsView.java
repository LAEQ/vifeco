package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class ControlsView extends AbstractJavaFXGriffonView {
    private ControlsController controller;
    private ControlsModel model;

    @MVCMember @Nonnull
    private VifecoView parentView;

    @FXML private Slider rateSlider;
    @FXML private Label rateLabel;

    @FXML private Slider volumeSlider;
    @FXML private Label volumeLabel;

    @FXML private Slider sizePointSlider;
    @FXML private Label sizePointLabel;

    @FXML private Slider durationPointSlider;
    @FXML private Label durationPointLabel;

    @FXML private Slider opacityPointSlider;
    @FXML private Label opacityPointLabel;

    @MVCMember
    public void setController(@Nonnull ControlsController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull ControlsModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();

        initRateSlider();
        initVolumeSpinner();
        initSizePoint();
        initDurationPoint();
        initOpacityPoint();

        parentView.getMiddlePane().getItems().add(node);
    }

    private void initOpacityPoint() {
        model.pointOpacityProperty().bindBidirectional(opacityPointSlider.valueProperty());
        opacityPointLabel.textProperty().bind(model.pointOpacityProperty().asString());
    }

    private void initDurationPoint() {
        model.pointDurationProperty().bindBidirectional(durationPointSlider.valueProperty());
        durationPointLabel.textProperty().bind(model.pointDurationProperty().asString());
    }

    private void initSizePoint() {
        model.pointSizeProperty().bindBidirectional(sizePointSlider.valueProperty());
        sizePointLabel.textProperty().bind(model.pointSizeProperty().asString());
    }

    private void initRateSlider(){
        model.rateProperty().bindBidirectional(rateSlider.valueProperty());
        rateLabel.textProperty().bind(model.rateProperty().asString());

        rateSlider.setValue(model.getRate());
        rateSlider.setShowTickMarks(true);
    }

    private void initVolumeSpinner(){
        model.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
        volumeLabel.textProperty().bind(model.volumeProperty().asString());
    }
}
