package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.metadata.ArtifactProviderFor;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;


@ArtifactProviderFor(GriffonView.class)
public class VifecoView extends AbstractJavaFXGriffonView {
    private VBox top;
    private SplitPane middlePane;
    private HBox bottom;

    @Nonnull
    public VBox getTop() {
        return top;
    }

    @Nonnull
    public SplitPane getMiddlePane() {
        return middlePane;
    }

    @Nonnull
    public HBox getBottom() {
        return bottom;
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args){
        createMVCGroup("menu");
        createMVCGroup("controls");
        createMVCGroup("videoList");
        createMVCGroup("player");
        createMVCGroup("category");
        createMVCGroup("bottom");
        createMVCGroup("database");
    }

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication().createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getConfiguration().getAsString("application.title"));
        stage.setScene(init());
        stage.sizeToScene();
        getApplication().getWindowManager().attach("mainWindow", stage);
    }

    private Scene init() {
        Scene scene = new Scene(new Group());
        scene.setFill(Color.WHITE);
        scene.setRoot(generateView());
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        return scene;
    }

    private VBox generateView(){
        VBox root = new VBox();
        root.setPrefWidth(1500);
        root.setPrefHeight(900);

        top = new VBox();
        top.setPrefWidth(900);
        top.setPrefHeight(102);
        root.setVgrow(top, Priority.NEVER);

        middlePane = new SplitPane();
        middlePane.prefHeight(-1);
        middlePane.prefWidth(-1);
        middlePane.setDividerPositions(0.3, 0.7);
//        middlePane.setStyle("-fx-border-color: black");
        root.setVgrow(middlePane, Priority.ALWAYS);


        bottom = new HBox();
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setSpacing(5);
        root.setVgrow(bottom, Priority.NEVER);

        root.getChildren().addAll(top, middlePane, bottom);

        return root;
    }
}
