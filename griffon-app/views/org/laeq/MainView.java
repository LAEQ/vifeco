package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;

@ArtifactProviderFor(GriffonView.class)
public class MainView extends AbstractJavaFXGriffonView {
    private MainController controller;
    private MainModel model;

    @FXML private VBox top;

    @MVCMember
    public void setController(@Nonnull MainController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull MainModel model) {
        this.model = model;
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args){
        createMVCGroup("test");
//        TestView view = (TestView) menu.getView();

//        System.out.println(menu);
//        System.out.println(main.getMvcId());
//        createMVCGroup("middle");
//        createMVCGroup("bottom");
    }

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication()
            .createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getConfiguration().getAsString("application.title"));
        stage.setScene(init());
        stage.sizeToScene();
        getApplication().getWindowManager().attach("main", stage);
    }

    public VBox getTop(){
        return this.top;
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
