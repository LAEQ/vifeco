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
import javafx.scene.layout.Pane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class ControlsView extends AbstractJavaFXGriffonView {
    private ControlsController controller;
    private ControlsModel model;

    @MVCMember @Nonnull
    private VifecoView parentView;

    @FXML
    private Pane rateSpinnerPane;

    private Spinner<String> spinner;

    @FXML Spinner<Integer> volumeSpinner;

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

        initRateSpinner();
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

    private void initRateSpinner(){
        spinner = new Spinner<String>();
        spinner.setEditable(false);

        SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory<String>() {
            @Override
            public void decrement(int steps) {
                model.decreateRate();
                this.setValue(model.getSpinnerRate());
            }

            @Override
            public void increment(int steps) {
                model.increaseRate();
                this.setValue(model.getSpinnerRate());
            }
        };

        valueFactory.setValue(model.getSpinnerRate());
        spinner.setValueFactory(valueFactory);

        rateSpinnerPane.getChildren().add(spinner);
        model.spinnerRateProperty().bindBidirectional(spinner.getEditor().textProperty());
    }

    private void initVolumeSpinner(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 10);

        valueFactory.valueProperty().addListener((observable, oldValue, newValue) -> {
            controller.volumeChangeEvent();
        });

        volumeSpinner.setValueFactory(valueFactory);
        model.volumeProperty().bindBidirectional(IntegerProperty.integerProperty(volumeSpinner.getValueFactory().valueProperty()));
        getLog().info(String.format("model volume: %d", model.getVolume()));
    }
}
