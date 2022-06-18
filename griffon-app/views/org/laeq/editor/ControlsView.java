package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.core.mvc.MVCGroup;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;

@ArtifactProviderFor(GriffonView.class)
public class ControlsView extends AbstractJavaFXGriffonView {
    private ControlsController controller;
    private ControlsModel model;

    @MVCMember @Nonnull private Controls controls;

    @FXML private Slider speed;
    @FXML private Slider size;
    @FXML private Slider duration;
    @FXML private Slider opacity;
    @FXML private Slider volume;

    @FXML private Label speedLabel;
    @FXML private Label durationLabel;
    @FXML private Label opacityLabel;
    @FXML private Label sizeLabel;
    @FXML private Label volumeLabel;

    @FXML private RadioButton durationUnder1s;
    @FXML private RadioButton durationOver1s;

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
        Stage stage = (Stage) getApplication()
            .createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getMessageSource().getMessage("z.controls"));
        stage.getIcons().add( getImage("favicon-32x32.png"));
        stage.setScene(init());
        stage.sizeToScene();
        stage.setAlwaysOnTop(false);
        getApplication().getWindowManager().attach("controls", stage);
        getApplication().getWindowManager().show("controls");


        stage.setOnCloseRequest(event -> {
            getApplication().getEventRouter().publishEventOutsideUI("mvc.clean", Arrays.asList("controls"));
        });

        initSpeedSlider();

        durationUnder1s.setOnAction(action -> {
            initDurationSlider(0.01d, 1d, 0.1d);
        });
        durationOver1s.selectedProperty().addListener(
            (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) ->{
            initDurationSlider(
                    controls.durationValue[0],
                    controls.durationValue[1],
                    5);
        });

        durationOver1s.setSelected(true);

        initSizeSlider();
        initOpacitySlider();
        initVolumeSlider();
    }

    private void initSpeedSlider() {
        speedLabel.setText(String.format("%.2f", controls.speed.getValue()));
        speed.valueProperty().bindBidirectional(controls.speed);
        speed.setMin(controls.speedValue[0]);
        speed.setMax(controls.speedValue[1]);
        speed.setMajorTickUnit(1);
        speed.setShowTickMarks(true);
        speed.setShowTickLabels(true);
        speed.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = Math.round(newVal.doubleValue() * 10) / 10f;
            speed.setValue(value);
            speedLabel.setText(String.format("%.2f", value));
            controller.dispatch("speed.change", Double.valueOf(value));
            controls.speed.set(value);
        });
    }

    private void initDurationSlider(double min, double max, double tickUnit) {
        durationLabel.setText(String.format("%.2f s", controls.duration.getValue()));
        duration.valueProperty().bindBidirectional(controls.duration);
        duration.setMin(min);
        duration.setMax(max);
        duration.setMajorTickUnit(tickUnit);
        duration.setShowTickMarks(true);
        duration.setShowTickLabels(true);
        duration.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = newVal.doubleValue();
            duration.setValue(value);
            durationLabel.setText(String.format("%.2f s", value));
            controller.dispatch("duration.change", Double.valueOf(value));
            controls.duration.set(value);
        });
    }

    private void initSizeSlider() {
        sizeLabel.setText(String.format("%.0f px", controls.size.getValue()));
        size.setValue(controls.size.getValue());
        size.setMin(controls.sizeValue[0]);
        size.setMax(controls.sizeValue[1]);
        size.setMajorTickUnit(5);
        size.setShowTickMarks(true);
        size.setShowTickLabels(true);
        size.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = Math.round(newVal.doubleValue() * 1) / 1f;
            size.setValue(value);
            sizeLabel.setText(String.format("%.0f px", value));
            controller.dispatch("size.change",  Double.valueOf(value));
            controls.size.setValue(value);
        });
    }

    private void initOpacitySlider() {
        opacityLabel.setText(String.format("%.1f", controls.opacity.getValue()));
        opacity.setValue(controls.opacity.getValue());
        opacity.setMin(controls.opacityValue[0]);
        opacity.setMax(controls.opacityValue[1]);
        opacity.setMajorTickUnit(.1);
        opacity.setShowTickMarks(true);
        opacity.setShowTickLabels(true);
        opacity.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = Math.round(newVal.doubleValue() * 10) / 10f;
            opacity.setValue(value);
            opacityLabel.setText(String.format("%.1f", value));
            controller.dispatch("opacity.change",  Double.valueOf(value));
            controls.opacity.setValue(value);
        });
    }

    private void initVolumeSlider() {
        volumeLabel.setText(String.format("%.1f", controls.volume.getValue()));
        volume.setValue(controls.volume.getValue());
        volume.setMin(controls.volumeValue[0]);
        volume.setMax(controls.volumeValue[1]);
        volume.setMajorTickUnit(.1);
        volume.setShowTickMarks(true);
        volume.setShowTickLabels(true);
        volume.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = Math.round(newVal.doubleValue() * 10) / 10f;
            volume.setValue(value);
            volumeLabel.setText(String.format("%.1f", value));
            controller.dispatch("volume.change",  Double.valueOf(value));
            controls.volume.setValue(value);
        });
    }

    private void closeAndDestroy(String name){
        destroy(name);
        closeScene(name);
    }
    private void destroy(String name) {
        try{
            MVCGroup group = getApplication().getMvcGroupManager().findGroup(name);
            if(group != null){
                group.destroy();
            }
        }catch (Exception e){

        }
    }
    private void closeScene(String name){
        try{
            Stage window = (Stage) getApplication().getWindowManager().findWindow(name);
            window.close();
            getApplication().getWindowManager().detach(name);
        }catch (Exception e){

        }
    }

    // build the UI
    private Scene init() {
        Scene scene = new Scene(new Group());
        scene.setFill(Color.WHITE);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

        Node node = loadFromFXML();
        if (node instanceof Parent) {
            scene.setRoot((Parent) node);
        } else {
            ((Group) scene.getRoot()).getChildren().addAll(node);
        }
        connectActions(node, controller);
        connectMessageSource(node);

        return scene;
    }

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }
}
