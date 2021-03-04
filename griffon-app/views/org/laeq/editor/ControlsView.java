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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import javax.annotation.Nonnull;
import java.util.*;

@ArtifactProviderFor(GriffonView.class)
public class ControlsView extends AbstractJavaFXGriffonView {
    private ControlsController controller;
    private ControlsModel model;

    @MVCMember @Nonnull private Controls controls;

    @FXML private Slider speed;
    @FXML private Slider size;
    @FXML private Slider duration;
    @FXML private Slider opacity;

    @FXML private Label speedLabel;
    @FXML private Label durationLabel;
    @FXML private Label opacityLabel;
    @FXML private Label sizeLabel;

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
        stage.setTitle("Controls");
        stage.setScene(init());
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);
        getApplication().getWindowManager().attach("controls", stage);
        getApplication().getWindowManager().show("controls");

        stage.setOnCloseRequest(event -> {
            closeAndDestroy("controls");
        });

        initSpeedSlider();
        initDurationSlider();
        initSizeSlider();
        initOpacitySlicer();
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
            double value = Math.round(newVal.doubleValue() * 4) / 4f;
            speed.setValue(value);
            speedLabel.setText(String.format("%.2f", value));
            controller.dispatch("speed.change", value);
        });
    }

    private void initDurationSlider() {
        durationLabel.setText(String.format("%.0f s", controls.duration.getValue()));
        duration.valueProperty().bindBidirectional(controls.duration);
        duration.setMin(controls.durationValue[0]);
        duration.setMax(controls.durationValue[1]);
        duration.setMajorTickUnit(5);
        duration.setShowTickMarks(true);
        duration.setShowTickLabels(true);
        duration.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = Math.round(newVal.doubleValue() * 1) / 1f;
            duration.setValue(value);
            durationLabel.setText(String.format("%.0f s", value));
            controller.dispatch("duration.change", value);
        });
    }

    private void initSizeSlider() {
        sizeLabel.setText(String.format("%.0f px", controls.size.getValue()));
        size.setMin(controls.sizeValue[0]);
        size.setMax(controls.sizeValue[1]);
        size.setMajorTickUnit(5);
        size.setShowTickMarks(true);
        size.setShowTickLabels(true);
        size.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = Math.round(newVal.doubleValue() * 1) / 1f;
            size.setValue(value);
            sizeLabel.setText(String.format("%.0f s", value));
            controller.dispatch("size.change", value);
        });
    }

    private void initOpacitySlicer() {
        opacityLabel.setText(String.format("%.0f", controls.opacity.getValue()));
        opacity.setMin(controls.opacityValue[0]);
        opacity.setMax(controls.opacityValue[1]);
        opacity.setMajorTickUnit(5);
        opacity.setShowTickMarks(true);
        opacity.setShowTickLabels(true);
        opacity.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = Math.round(newVal.doubleValue() * 10) / 10f;
            opacity.setValue(value);
            opacityLabel.setText(String.format("%.1f", value));
            controller.dispatch("opacity.change", value);
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
}
