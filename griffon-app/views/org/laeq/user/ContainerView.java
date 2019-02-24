package org.laeq.user;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.graphic.Color;
import org.laeq.graphic.IconSVG;
import org.laeq.model.Icon;
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
        TableColumn<User, Void> actionsColumn = new TableColumn<>("Actions");

        userTable.getColumns().addAll(firstNameColumn, lastNameColumn, emailColumn, defaultColumn, actionsColumn);

        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        defaultColumn.setCellValueFactory(cellData -> cellData.getValue().isActiveProperty());
        actionsColumn.setCellFactory(addActions());

        model.firstNameProperty().bindBidirectional(firstNameField.textProperty());
        model.lastNameProperty().bindBidirectional(lastNameField.textProperty());
        model.emailProperty().bindBidirectional(emailField.textProperty());


        userTable.setItems(model.getUserList());
    }

    private Callback<TableColumn<User, Void>, TableCell<User, Void>> addActions() {
        return param -> {
           final  TableCell<User, Void> cell = new TableCell<User, Void>(){
               Button edit = new Button("");
               Button delete = new Button("");

               Group btnGroup = new Group();
               {
                   edit.setLayoutX(5);
                   delete.setLayoutX(55);

                   btnGroup.getChildren().addAll(edit, delete);
                   Icon icon = new Icon(IconSVG.edit, Color.gray_dark);
                   edit.setGraphic(icon);
                   edit.setOnAction(event -> {
                       model.setSelectedUser(userTable.getItems().get(getIndex()));
                   });


                   delete.setGraphic(new Icon(IconSVG.bin, Color.gray_dark));
                   delete.setOnAction(event -> {
                       controller.delete(userTable.getItems().get(getIndex()));
                   });
               }

               @Override
               public void updateItem(Void item, boolean empty) {
                   super.updateItem(item, empty);
                   if (empty) {
                       setGraphic(null);
                   } else {
                       setGraphic(btnGroup);
                   }
               }
           };

           return cell;
        };
    }
}
