package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.reactfx.Subscription;
import javax.annotation.Nonnull;
import java.util.*;

@ArtifactProviderFor(GriffonView.class)
public class EditorView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private EditorController controller;
    @MVCMember @Nonnull private EditorModel model;
    @MVCMember @Nonnull private Video video;

    public Scene scene;

    @FXML public Label title;
    @FXML public AnchorPane timeline;
    @FXML public AnchorPane summary;

    //Video player
    private MediaPlayer mediaPlayer;
    @FXML public Pane playerPane;

    @FXML public AnchorPane playerControls;
    @FXML public Pane iconPane;
    @FXML public Pane mediaPane;
    @FXML public Pane tools;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args){
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("video", args.get("video"));
        createMVCGroup("player", arguments);
        createMVCGroup("editor_tools", arguments);

        createMVCGroup("timeline", arguments);
        createMVCGroup("category_sum", arguments);
        createMVCGroup("icon_pane", arguments);
        createMVCGroup("player_controls", arguments);
    }

    @Override
    public void mvcGroupDestroy(){

    }

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication().createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getMessageSource().getMessage("editor.window.title"));
        stage.getIcons().add( getImage("favicon-32x32.png"));
        scene = init();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);

        getApplication().getWindowManager().attach("editor", stage);
        getApplication().getWindowManager().show("editor");

        stage.setOnCloseRequest(event -> {
            getApplication().getMvcGroupManager().findGroup("editor").destroy();
        });

        scene.setOnKeyReleased(keyReleased());
    }

    private Scene init() {
        final Scene scene = new Scene(new Group());
        scene.setFill(Color.WHITE);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

        final Node node = loadFromFXML();

        if (node instanceof Parent) {
            scene.setRoot((Parent) node);
        } else {
            ((Group) scene.getRoot()).getChildren().addAll(node);
        }
        connectActions(node, controller);
        connectMessageSource(node);

        title.setText(String.format("%s - %s", video.pathToName(), DurationFormatUtils.formatDuration((long) video.getDuration().toMillis(), "HH:mm:ss")));

        return scene;
    }

    private EventHandler<? super KeyEvent> keyReleased() {

        return (EventHandler<KeyEvent>) event -> {
            if(event.getCode() == KeyCode.CONTROL){
                controller.togglePlay();
            } else {
                controller.addPoint(event.getCode());
            }
        };
    }

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }


    public void removePoint(Point point) {
        getApplication().getEventRouter().publishEventOutsideUI("point.removed", Arrays.asList(point));
    }
}
