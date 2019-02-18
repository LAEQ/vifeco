package org.laeq.user;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.User;
import org.laeq.template.MiddlePaneView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class ContainerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private TableView<User> userTable;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        init();
    }

    private void init(){
        TableColumn<User, String> firstNameColumn = new TableColumn("First name");
        TableColumn<User, String> lastNameColumn = new TableColumn<>("Last name");
        TableColumn<User, String> emailColumn = new TableColumn("Email");
        TableColumn<User, Boolean> defaultColumn = new TableColumn("Is default");

        userTable.getColumns().addAll(firstNameColumn, lastNameColumn, emailColumn, defaultColumn);

        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        defaultColumn.setCellValueFactory(cellData -> cellData.getValue().isActiveProperty());

        model.firstNameProperty().bindBidirectional(firstNameField.textProperty());
        model.lastNameProperty().bindBidirectional(lastNameField.textProperty());
        model.emailProperty().bindBidirectional(emailField.textProperty());

        userTable.setItems(model.getUserList());

        userTable.setOnMouseClicked(event -> {
            model.setSelectedUser(userTable.getSelectionModel().getSelectedItem());
        });
    }
}
