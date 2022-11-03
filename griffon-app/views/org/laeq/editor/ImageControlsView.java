package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.core.mvc.MVCGroup;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;

@ArtifactProviderFor(GriffonView.class)
public class ImageControlsView extends AbstractJavaFXGriffonView {
    private ImageControlsController controller;
    private ImageControlsModel model;

    @MVCMember @Nonnull private ImageControls controls;

    @FXML private Slider brightness;
    @FXML private Slider saturation;
    @FXML private Slider contrast;
    @FXML private Slider hue;

    @FXML private Label brightnessLabel;
    @FXML private Label saturationLabel;
    @FXML private Label contrastLabel;
    @FXML private Label hueLabel;

    @MVCMember
    public void setController(@Nonnull ImageControlsController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull ImageControlsModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication()
            .createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getMessageSource().getMessage("z.image_controls"));
        stage.getIcons().add( getImage("favicon-32x32.png"));
        stage.setScene(init());
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);
        getApplication().getWindowManager().attach("image_controls", stage);
        getApplication().getWindowManager().show("image_controls");


        stage.setOnCloseRequest(event -> {
            getApplication().getEventRouter().publishEventOutsideUI("mvc.clean", Arrays.asList("image_controls"));
            getApplication().getEventRouter().publishEventOutsideUI("image_controls.reset");
        });

        initBrightnessSlider();
        initSaturationSlider();
        initContrastSlider();
        initHueSlider();
    }

    private void initBrightnessSlider() {
        brightnessLabel.setText(String.format("%.2f", controls.brightness.getValue()));
        brightness.valueProperty().bindBidirectional(controls.brightness);
        brightness.setMin(controls.brightnessValue[0]);
        brightness.setMax(controls.brightnessValue[1]);
        brightness.setMajorTickUnit(.1);
        brightness.setShowTickMarks(true);
        brightness.setShowTickLabels(true);
        brightness.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = Math.round(newVal.doubleValue() * 10) / 10f;
            brightness.setValue(value);
            brightnessLabel.setText(String.format("%.2f", value));
            controller.dispatch("brightness.change", Double.valueOf(value));
            controls.brightness.set(value);
        });
    }

    private void initSaturationSlider() {
        saturationLabel.setText(String.format("%.2f", controls.saturation.getValue()));
        saturation.setValue(controls.saturation.getValue());
        saturation.setMin(controls.saturationValue[0]);
        saturation.setMax(controls.saturationValue[1]);
        saturation.setMajorTickUnit(0.1);
        saturation.setShowTickMarks(true);
        saturation.setShowTickLabels(true);
        saturation.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = Math.round(newVal.doubleValue() * 10) / 10f;
            saturation.setValue(value);
            saturationLabel.setText(String.format("%.2f", value));
            controller.dispatch("saturation.change",  Double.valueOf(value));
            controls.saturation.setValue(value);
        });
    }

    private void initContrastSlider() {
        contrastLabel.setText(String.format("%.2f", controls.saturation.getValue()));
        contrast.setValue(controls.saturation.getValue());
        contrast.setMin(controls.contrastValue[0]);
        contrast.setMax(controls.contrastValue[1]);
        contrast.setMajorTickUnit(0.1);
        contrast.setShowTickMarks(true);
        contrast.setShowTickLabels(true);
        contrast.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = Math.round(newVal.doubleValue() * 10) / 10f;
            contrast.setValue(value);
            contrastLabel.setText(String.format("%.2f", value));
            controller.dispatch("constrast.change",  Double.valueOf(value));
            controls.contrast.setValue(value);
        });
    }

    private void initHueSlider() {
        hueLabel.setText(String.format("%.2f", controls.hue.getValue()));
        hue.setValue(controls.hue.getValue());
        hue.setMin(controls.hueValue[0]);
        hue.setMax(controls.hueValue[1]);
        hue.setMajorTickUnit(0.1);
        hue.setShowTickMarks(true);
        hue.setShowTickLabels(true);
        hue.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = Math.round(newVal.doubleValue() * 10) / 10f;
            hue.setValue(value);
            hueLabel.setText(String.format("%.2f", value));
            controller.dispatch("hue.change",  Double.valueOf(value));
            controls.hue.setValue(value);
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
