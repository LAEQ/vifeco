package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.graphic.Color;
import org.laeq.graphic.IconSVG;
import org.laeq.model.Icon;
import org.laeq.model.IconButton;
import org.laeq.video.player.ContainerView;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonView.class)
public class ControlsView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ControlsController controller;
    @MVCMember @Nonnull private ControlsModel model;
    @MVCMember @Nonnull private ContainerView parentView;

    @FXML private Slider rateSlider;
    @FXML private Text rateValue;
    @FXML private Pane rateIcon;

    @FXML private Slider volumeSlider;
    @FXML private Text volumeValue;
    @FXML private Pane volumeIcon;

    @FXML private Slider sizeSlider;
    @FXML private Text sizeValue;
    @FXML private Pane sizeIcon;

    @FXML private Slider durationSlider;
    @FXML private Text durationValue;
    @FXML private Pane durationIcon;

    @FXML private Slider opacitySlider;
    @FXML private Text opacityValue;
    @FXML private Pane opacityIcon;

    private Map<String, ChangeListener<Number>> listenerMap;
    private Map<String, EventHandler<MouseEvent>> clickListener;

    @Override
    public void initUI() {
        Node node = loadFromFXML();

        initRateSlider();
        initVolumeSpinner();
        initSizePoint();
        initDurationPoint();
        initOpacityPoint();

        parentView.getControlPane().getChildren().add(node);

        listenerMap = initListener();
        rateSlider.valueProperty().addListener(listenerMap.get("rate"));
        volumeSlider.valueProperty().addListener(listenerMap.get("volume"));
        opacitySlider.valueProperty().addListener(listenerMap.get("opacity"));
        durationSlider.valueProperty().addListener(listenerMap.get("duration"));
        sizeSlider.valueProperty().addListener(listenerMap.get("size"));

        clickListener = clickListener();
        rateIcon.setOnMouseClicked(clickListener.get("rate"));
        volumeIcon.setOnMouseClicked(clickListener.get("volume"));
        sizeIcon.setOnMouseClicked(clickListener.get("size"));
        opacityIcon.setOnMouseClicked(clickListener.get("opacity"));
        durationIcon.setOnMouseClicked(clickListener.get("duration"));
    }

    @Override
    public void mvcGroupDestroy() {
        runInsideUISync(() -> {
            destroy();
        });
    }

    public Text getRateValue() {
        return rateValue;
    }

    public Text getVolumeValue() {
        return volumeValue;
    }

    public Text getSizeValue() {
        return sizeValue;
    }

    public Text getDurationValue() {
        return durationValue;
    }

    public Text getOpacityValue() {
        return opacityValue;
    }

    private void destroy(){
        rateSlider.valueProperty().removeListener(listenerMap.get("rate"));
        volumeSlider.valueProperty().removeListener(listenerMap.get("volume"));
        sizeSlider.valueProperty().removeListener(listenerMap.get("size"));
        opacitySlider.valueProperty().removeListener(listenerMap.get("opacity"));
        durationSlider.valueProperty().removeListener(listenerMap.get("duration"));

        rateIcon.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickListener.get("rate"));
        volumeIcon.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickListener.get("volume"));
        sizeIcon.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickListener.get("size"));
        opacityIcon.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickListener.get("opacity"));
        durationIcon.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickListener.get("duration"));

        listenerMap.clear();
    }

    private Map<String, EventHandler<MouseEvent>>  clickListener(){
        Map<String, EventHandler<MouseEvent>> list = new HashMap<>();

        list.put("rate",  event -> controller.changeRate(model.setDefaultRate()));
        list.put("volume",  event -> controller.changeVolume(model.setDefaultVolume()));
        list.put("opacity",  event -> controller.changeOpacity(0.5, model.setDefaultOpacity()));
        list.put("size",  event -> controller.changeSize(model.setDefaultSize()));
        list.put("duration",  event -> controller.changeDuration(model.setDefaultDuration()));

        return list;
    }

    private Map<String, ChangeListener<Number>> initListener(){
        Map<String, ChangeListener<Number>> list = new HashMap<>();

        list.put("rate", (observable, oldValue, newValue) -> {
            controller.changeRate(newValue);
        });

        list.put("volume", (observable, oldValue, newValue) -> {
            controller.changeVolume(newValue);
        });

        list.put("size", (observable, oldValue, newValue) -> {
            controller.changeSize(newValue);
        });

        list.put("duration", (observable, oldValue, newValue) -> {
            System.out.println("duration:" + newValue);
            controller.changeDuration(newValue);
        });

        list.put("opacity", (observable, oldValue, newValue) -> {
            controller.changeOpacity(oldValue, newValue);
        });

        return list;
    }

    private void initRateSlider(){
        Icon icon = new IconButton(IconSVG.rate, Color.gray_dark);
        rateIcon.getChildren().add(icon);
        rateSlider.valueProperty().bindBidirectional(model.rateProperty());
        rateValue.textProperty().bind(model.rateProperty().asString());

        rateSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            BigDecimal bd = new BigDecimal((newVal.toString()));
            bd = bd.setScale(1, RoundingMode.HALF_EVEN);
            rateSlider.setValue(bd.doubleValue());
        });

        rateSlider.setMin(0.1);
        rateSlider.setMax(10);
        rateSlider.setMajorTickUnit(1);
        rateSlider.setShowTickMarks(true);
        rateSlider.setShowTickLabels(true);
    }

    private void initVolumeSpinner(){
        Icon icon = new IconButton(IconSVG.volume, Color.gray_dark);
        volumeIcon.getChildren().add(icon);
        volumeSlider.valueProperty().bindBidirectional(model.volumeProperty());
        volumeValue.textProperty().bind(model.volumeProperty().asString());

        volumeSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            BigDecimal bd = new BigDecimal((newVal.toString()));
            bd = bd.setScale(1, RoundingMode.HALF_EVEN);
            volumeSlider.setValue(bd.doubleValue());
        });

        volumeSlider.setMin(0);
        volumeSlider.setMax(1);
        volumeSlider.setMajorTickUnit(0.2);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setShowTickLabels(true);
    }

    private void initSizePoint() {
        Icon icon = new IconButton(IconSVG.size, Color.gray_dark);
        sizeIcon.getChildren().add(icon);
        sizeSlider.valueProperty().bindBidirectional(model.sizeProperty());
        sizeValue.textProperty().bind(model.sizeProperty().asString());

        sizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            BigDecimal bd = new BigDecimal(newValue.toString());
            bd = bd.setScale(0, RoundingMode.HALF_EVEN);
            sizeSlider.setValue(bd.intValueExact());
        });

        sizeSlider.setMin(10);
        sizeSlider.setMax(100);
        sizeSlider.setMajorTickUnit(20);
        sizeSlider.setSnapToTicks(true);
        sizeSlider.setShowTickMarks(true);
        sizeSlider.setShowTickLabels(true);
    }

    private void initOpacityPoint() {
        Icon icon = new IconButton(IconSVG.opacity, Color.gray_dark);
        opacityIcon.getChildren().add(icon);
        opacitySlider.valueProperty().bindBidirectional(model.opacityProperty());
        opacityValue.textProperty().bind(model.opacityProperty().asString());

        opacitySlider.valueProperty().addListener((obs, oldval, newVal) -> {
            BigDecimal bd = new BigDecimal((newVal.toString()));
            bd = bd.setScale(1, RoundingMode.HALF_EVEN);
            opacitySlider.setValue(bd.doubleValue());
        });

        opacitySlider.setMin(0.1);
        opacitySlider.setMax(1);
        opacitySlider.setMajorTickUnit(0.1);
        opacitySlider.setShowTickMarks(true);
        opacitySlider.setShowTickLabels(true);

    }

    private void initDurationPoint() {
        Icon icon = new IconButton(IconSVG.duration, Color.gray_dark);
        durationIcon.getChildren().add(icon);
        durationSlider.valueProperty().bindBidirectional(model.durationProperty());
        durationValue.textProperty().bind(model.durationProperty().asString());

        durationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            BigDecimal bd = new BigDecimal(newValue.toString());
            bd = bd.setScale(0, RoundingMode.HALF_EVEN);
            durationSlider.setValue(bd.intValueExact());
        });

        durationSlider.setMin(1);
        durationSlider.setMax(10);
        durationSlider.setMajorTickUnit(2);
        durationSlider.setShowTickMarks(true);
        durationSlider.setShowTickLabels(true);
    }
}
