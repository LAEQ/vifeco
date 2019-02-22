package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;

@ArtifactProviderFor(GriffonView.class)
public class ControlsView extends AbstractJavaFXGriffonView {
    private ControlsController controller;
    private ControlsModel model;

    @MVCMember @Nonnull
    private Player2View parentView;

    @FXML private Pane sliderPanel;

    @FXML private Slider rateSlider;
    @FXML private Text rateValue;

    @FXML private Slider volumeSlider;
    @FXML private Text volumeValue;

    @FXML private Slider sizePointSlider;
    @FXML private Text sizePointValue;

    @FXML private Slider durationPointSlider;
    @FXML private Text durationPointValue;

    @FXML private Slider opacityPointSlider;
    @FXML private Text opacityPointValue;

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

        Pane pane = new Pane();
        pane.getChildren().add(node);

        AnchorPane.setLeftAnchor(pane, 0d);
        AnchorPane.setRightAnchor(pane, 0d);
        AnchorPane.setBottomAnchor(pane, 0d);
        AnchorPane.setTopAnchor(pane, 0d);

        parentView.getControlPane().getChildren().add(pane);
    }

    private Group generateSlider(){
        Group group = new Group();

        return group;
    }

    private void setSliderOptions(Slider slider, Double value){
//        slider.setValue(value);
//        DoubleProperty integerProperty = new SimpleDoubleProperty(model.getVolume() / 10);
//        ObjectProperty<Integer> objectProperty = new SimpleObjectProperty<>(2);
//        // Need to keep the reference as bidirectional binding uses weak references
//        IntegerProperty objectAsInteger = IntegerProperty.integerProperty(objectProperty);
//        integerProperty.bindBidirectional(objectAsInteger);
    }

    private void initOpacityPoint() {
        opacityPointSlider.valueProperty().bindBidirectional(model.pointOpacityProperty());
        opacityPointValue.textProperty().bind(model.pointOpacityProperty().asString());

        opacityPointSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            BigDecimal bd = new BigDecimal((newVal.toString()));
            bd = bd.setScale(1, RoundingMode.HALF_EVEN);
            opacityPointSlider.setValue(bd.doubleValue());
        });

    }

    private void initDurationPoint() {
        durationPointSlider.valueProperty().bindBidirectional(model.pointDurationProperty());
        durationPointValue.textProperty().bind(model.pointDurationProperty().asString());

        durationPointSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            BigDecimal bd = new BigDecimal(newValue.toString());
            bd = bd.setScale(0, RoundingMode.HALF_EVEN);
            durationPointSlider.setValue(bd.intValueExact());
        });

    }

    private void initSizePoint() {
        sizePointSlider.valueProperty().bindBidirectional(model.pointSizeProperty());
        sizePointValue.textProperty().bind(model.pointSizeProperty().asString());
    }

    private void initRateSlider(){
        rateSlider.valueProperty().bindBidirectional(model.rateProperty());
        rateValue.textProperty().bind(model.rateProperty().asString());

        rateSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            BigDecimal bd = new BigDecimal((newVal.toString()));
            bd = bd.setScale(1, RoundingMode.HALF_EVEN);
            rateSlider.setValue(bd.doubleValue());
        });
    }
    private void initVolumeSpinner(){
        volumeSlider.valueProperty().bindBidirectional(model.volumeProperty());
        volumeValue.textProperty().bind(model.volumeProperty().asString());

        volumeSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            BigDecimal bd = new BigDecimal((newVal.toString()));
            bd = bd.setScale(1, RoundingMode.HALF_EVEN);
            volumeSlider.setValue(bd.doubleValue());
        });
    }
}
