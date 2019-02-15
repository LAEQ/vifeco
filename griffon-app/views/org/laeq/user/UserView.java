package org.laeq.user;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.laeq.video.VideoListView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class UserView extends AbstractJavaFXGriffonView {
    private UserController controller;
    private UserModel model;
    private Tab tab;
    private TabPane parentPane;

    @MVCMember @Nonnull private VideoListView parentView;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;


    @MVCMember
    public void setController(@Nonnull UserController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull UserModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentPane = parentView.getTabPane();

        tab = new Tab();
        tab.setGraphic(new FontIcon(FontAwesome.USER_PLUS));
        tab.setText("New User");
        tab.setId("user_new");
        tab.setContent(node);
        tab.setClosable(true);

        tab.setOnClosed(closeTab());

        connectActions(node, controller);

        parentPane.getTabs().add(tab);
        parentPane.getSelectionModel().select(tab);

        model.firstNameProperty().bindBidirectional(firstNameField.textProperty());
        model.lastNameProperty().bindBidirectional(lastNameField.textProperty());
        model.emailProperty().bindBidirectional(emailField.textProperty());
    }

    private EventHandler<Event> closeTab(){
        return event -> controller.closeTab();
    }

    @Override
    public void mvcGroupDestroy(){
        runInsideUISync(()-> {
            parentPane.getTabs().remove(tab);
        });
    }

}
