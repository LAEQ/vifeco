package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.User;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class MenuView extends AbstractJavaFXGriffonView {
    private MenuController controller;
    private MenuModel model;



    @FXML private ComboBox<User> userComboBox;

    @MVCMember @Nonnull
    private VifecoView parentView;

    @MVCMember
    public void setController(@Nonnull MenuController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull MenuModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();


        connectActions(node, controller);

        parentView.getTop().getChildren().add(node);
    }

    @FXML
    private void handleUserCombo(){
        controller.setActiveUser(userComboBox.getSelectionModel().getSelectedItem());
    }

    public ComboBox<User> getUserComboBox() {
        return userComboBox;
    }
}
