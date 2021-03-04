package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class TestView extends AbstractJavaFXGriffonView {
    private TestController controller;
    private TestModel model;

    @MVCMember @Nonnull private MainView parentView;

    @FXML
    private Label clickLabel;

    @MVCMember
    public void setController(@Nonnull TestController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull TestModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        connectActions(node, controller);
        model.clickCountProperty().bindBidirectional(clickLabel.textProperty());
        connectMessageSource(node);
//        init();
        System.out.println("InitUI test view");

        parentView.getTop().getChildren().add(node);

//        Stage stage = (Stage) getApplication()
//            .createApplicationContainer(Collections.<String,Object>emptyMap());
//        stage.setTitle(getApplication().getConfiguration().getAsString("application.title"));
//        stage.setScene(init());
//        stage.sizeToScene();
//        getApplication().getWindowManager().attach("test", stage);
    }
}
