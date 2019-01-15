package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import java.util.Collections;
import java.util.Map;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class VifecoView extends AbstractJavaFXGriffonView {
    private BorderPane borderPane;

    @Nonnull
    public BorderPane getBorderPane(){
        return borderPane;
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args){
        createMVCGroup("menu");
    }

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication()
            .createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getConfiguration().getAsString("application.title"));
        stage.setScene(init());
        stage.sizeToScene();
        getApplication().getWindowManager().attach("mainWindow", stage);
    }

    private Scene init() {
        Scene scene = new Scene(new Group());
        scene.setFill(Color.WHITE);

        borderPane = new BorderPane();
        borderPane.setPrefWidth(1366);
        borderPane.setPrefHeight(768);
        scene.setRoot(borderPane);


        return scene;
    }
}
