package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.editor.Controls;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonView.class)
public class VifecoView extends AbstractJavaFXGriffonView {
    @MVCMember
    private VifecoController controller;

    @MVCMember
    private void setController(@Nonnull VifecoController controller){
        this.controller = controller;
    }

    @Inject private DatabaseService dbService;

    @FXML public Pane menu;
    @FXML public AnchorPane middle;
    @FXML public AnchorPane bottom;

    public Scene scene;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args){
        createMVCGroup("menu");
        createMVCGroup("video");
        createMVCGroup("bottom");
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
        scene = new Scene(new Group());
        scene.setFill(Color.WHITE);

        Node node = loadFromFXML();

        if (node instanceof Parent) {
            scene.setRoot((Parent) node);
        } else {
            ((Group) scene.getRoot()).getChildren().addAll(node);
        }
        connectActions(node, controller);
        connectMessageSource(node);

        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

        return scene;

    }
}
