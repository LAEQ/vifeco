package org.laeq.editor.filter;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

@ArtifactProviderFor(GriffonView.class)
public class DisplayView extends AbstractJavaFXGriffonView {

    @MVCMember @Nonnull private DisplayController controller;
    @MVCMember @Nonnull private DisplayModel model;

    @FXML private Accordion filters;

    @FXML private TitledPane altmRetinexPane;

    @FXML private Slider redParam;
    @FXML private Label redParamLabel;

    @FXML private Slider greenParam;
    @FXML private Label greenParamLabel;

    @FXML private Slider blueParam;
    @FXML private Label blueParamLabel;

    @FXML private ImageView imageView;

    @FXML private Button ALTMRetinexBtn;

    public AltmRetinexControls controls = new AltmRetinexControls();

    private static final int r = 10;
    private static final double eps = 0.01;
    private static final double eta = 36.0;
    private static final double lambda = 100.0;
    private static final double krnlRatio = 0.01;

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication()
                .createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getMessageSource().getMessage("z.controls"));
        stage.getIcons().add( getImage("favicon-32x32.png"));
        stage.setScene(init());
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);
        getApplication().getWindowManager().attach("video_transform", stage);
        getApplication().getWindowManager().show("video_transform");

        stage.setOnCloseRequest(event -> {
            getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("video_transform"));
        });

    }

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

        initRedParamSlider();
        initGreenParamSlider();
        initBlueParamSlider();

        filters.setExpandedPane(altmRetinexPane);


        return scene;
    }

    private void initRedParamSlider() {
        redParamLabel.setText(String.format("%.01f", 0.5));
        redParam.valueProperty().bindBidirectional(controls.rParam);
        redParam.setMin(controls.rParamValue[0]);
        redParam.setMax(controls.rParamValue[1]);
        redParam.setMajorTickUnit(.01);
        redParam.setShowTickMarks(true);
        redParam.setShowTickLabels(true);
        redParam.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = newVal.doubleValue();
            redParam.setValue(value);
            redParamLabel.setText(String.valueOf(value));
        });
    }

    private void initGreenParamSlider() {
        greenParamLabel.setText(String.format("%.01f", 0.5));
        greenParam.valueProperty().bindBidirectional(controls.gParam);
        greenParam.setMin(controls.gParamValue[0]);
        greenParam.setMax(controls.gParamValue[1]);
        greenParam.setMajorTickUnit(.01);
        greenParam.setShowTickMarks(true);
        greenParam.setShowTickLabels(true);
        greenParam.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = newVal.doubleValue();
            greenParam.setValue(value);
            greenParamLabel.setText(String.valueOf(value));
        });
    }

    private void initBlueParamSlider() {
        blueParamLabel.setText(String.format("%.01f", 0.5));
        blueParam.valueProperty().bindBidirectional(controls.bParam);
        blueParam.setMin(controls.bParamValue[0]);
        blueParam.setMax(controls.bParamValue[1]);
        blueParam.setMajorTickUnit(.01);
        blueParam.setShowTickMarks(true);
        blueParam.setShowTickLabels(true);
        blueParam.valueProperty().addListener((obs, oldval, newVal) -> {
            double value = newVal.doubleValue();
            blueParam.setValue(value);
            blueParamLabel.setText(String.valueOf(value));
        });
    }

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }
}
