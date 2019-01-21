package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                getLog().info(String.format("%d : %f\n", model.getVolume(), model.getVolumeRate()));
//                System.out.println(model.getVolume());
            }
        });

        parentView.getMiddlePane().getItems().add(node);
    }

    private void setSliderOptions(Slider slider, Double value){
        slider.setValue(value);
//        slider.setShowTickLabels(true);
//        slider.setShowTickMarks(true);
//        slider.setMajorTickUnit(5);
//        slider.setMinorTickCount(1);

        DoubleProperty integerProperty = new SimpleDoubleProperty(model.getVolume() / 10);
        ObjectProperty<Integer> objectProperty = new SimpleObjectProperty<>(2);

        // Need to keep the reference as bidirectional binding uses weak references
        IntegerProperty objectAsInteger = IntegerProperty.integerProperty(objectProperty);

        integerProperty.bindBidirectional(objectAsInteger);
    }

    private void initOpacityPoint() {
        setSliderOptions(opacityPointSlider, 0.3);
        model.pointOpacityProperty().bindBidirectional(opacityPointSlider.valueProperty());
        opacityPointLabel.textProperty().bind(model.pointOpacityProperty().asString());
    }

    private void initDurationPoint() {
        setSliderOptions(durationPointSlider, 0.3);
        model.pointDurationProperty().bindBidirectional(durationPointSlider.valueProperty());
        durationPointLabel.textProperty().bind(model.pointDurationProperty().asString());
    }

    private void initSizePoint() {
        setSliderOptions(sizePointSlider, 10.0);
        model.pointSizeProperty().bindBidirectional(sizePointSlider.valueProperty());
        sizePointLabel.textProperty().bind(model.pointSizeProperty().asString());
    }

    private void initRateSlider(){
        setSliderOptions(rateSlider, 1.0);
        model.rateProperty().bindBidirectional(rateSlider.valueProperty());
        rateLabel.textProperty().bind(model.rateProperty().asString());

        rateSlider.setValue(model.getRate());
        rateSlider.setShowTickMarks(true);
    }

    private void initVolumeSpinner(){
        setSliderOptions(volumeSlider, 100.3);
        model.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
        volumeLabel.textProperty().bind(model.volumeProperty().asString());
    }
}
