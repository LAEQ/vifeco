package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.laeq.model.Icon;
import org.laeq.model.User;
import org.laeq.model.icon.Color;
import org.laeq.model.icon.IconSVG;
import org.laeq.template.MiddlePaneView;
import org.laeq.user.PreferencesService;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;

@ArtifactProviderFor(GriffonView.class)
public class UserView extends TranslatedView {
    @MVCMember @Nonnull private UserController controller;
    @MVCMember @Nonnull private UserModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> id;
    @FXML private TableColumn<User, String> firstName;
    @FXML private TableColumn<User, String> lastName;
    @FXML private TableColumn<User, String> email;
    @FXML private TableColumn<User, Icon> isDefault;
    @FXML private TableColumn<User, Void> actions;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private Button saveActionTarget;
    @FXML private Button clearActionTarget;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        connectMessageSource(node);
        init();
    }

    private void init(){
        id.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getId().toString()));
        firstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        isDefault.setCellValueFactory(cellData -> cellData.getValue().getDefault() ? new SimpleObjectProperty<>(new Icon(IconSVG.tick, Color.green)) : null);
        actions.setCellFactory(addActions());

        model.firstName.bindBidirectional(firstNameField.textProperty());
        model.lastName.bindBidirectional(lastNameField.textProperty());
        model.email.bindBidirectional(emailField.textProperty());

        userTable.setItems(model.userList);
    }

    private Callback<TableColumn<User, Void>, TableCell<User, Void>> addActions() {
        return param -> {
           final  TableCell<User, Void> cell = new TableCell<User, Void>(){
               Button edit = new Button("edit");
               Button delete = new Button("delete");

               Group btnGroup = new Group();
               {
                   btnGroup.getChildren().addAll(edit, delete);
                   edit.setLayoutX(5);
                   edit.getStyleClass().addAll("btn", "btn-sm", "btn-info");
                   delete.setLayoutX(105);
                   delete.getStyleClass().addAll("btn", "btn-sm", "btn-danger");


                   Icon icon = new Icon(IconSVG.edit, Color.gray_dark);
//                   edit.setGraphic(icon);
                   edit.setOnAction(event -> {
                       model.setSelectedUser(userTable.getItems().get(getIndex()));
                   });

//                   delete.setGraphic(new Icon(IconSVG.bin, Color.gray_dark));
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
