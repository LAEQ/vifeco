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

    @FXML private Label brightnessLabel;
    @FXML private Label saturationLabel;


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
        stage.setTitle(getApplication().getMessageSource().getMessage("z.controls"));
        stage.getIcons().add( getImage("favicon-32x32.png"));
        stage.setScene(init());
        stage.sizeToScene();
        stage.setAlwaysOnTop(false);
        getApplication().getWindowManager().attach("controls", stage);
        getApplication().getWindowManager().show("controls");


        stage.setOnCloseRequest(event -> {
            getApplication().getEventRouter().publishEventOutsideUI("mvc.clean", Arrays.asList("image_controls"));
        });

        initBrightnessSlider();
        initSaturationSlider();
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

    private void initOpacitySlider() {
//        opacityLabel.setText(String.format("%.1f", controls.opacity.getValue()));
//        opacity.setValue(controls.opacity.getValue());
//        opacity.setMin(controls.opacityValue[0]);
//        opacity.setMax(controls.opacityValue[1]);
//        opacity.setMajorTickUnit(.1);
//        opacity.setShowTickMarks(true);
//        opacity.setShowTickLabels(true);
//        opacity.valueProperty().addListener((obs, oldval, newVal) -> {
//            double value = Math.round(newVal.doubleValue() * 10) / 10f;
//            opacity.setValue(value);
//            opacityLabel.setText(String.format("%.1f", value));
//            controller.dispatch("opacity.change",  Double.valueOf(value));
//            controls.opacity.setValue(value);
//        });
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
