package org.laeq.user;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.laeq.model.User;
import org.laeq.model.VideoUser;
import org.laeq.user.UserListController;
import org.laeq.user.UserListModel;
import org.laeq.video.VideoListView;

import java.util.Collections;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class UserListView extends AbstractJavaFXGriffonView {
    private UserListController controller;
    private UserListModel model;
    private TabPane parentPane;
    private Tab tab;

    @MVCMember @Nonnull private VideoListView  parentView;

    @FXML private TableView<User> userTable;

    @MVCMember
    public void setController(@Nonnull UserListController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull UserListModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        parentPane = parentView.getTabPane();
        Node node = loadFromFXML();

        tab = new Tab();
        tab.setGraphic(new FontIcon(FontAwesome.USER));
        tab.setText("User list");
        tab.setId("user_list");
        tab.setContent(node);
        tab.setClosable(true);

        tab.setOnClosed(closeTab());

        connectActions(node, controller);

//        AnchorPane.setTopAnchor(node, 0.0);
//        AnchorPane.setBottomAnchor(node, 0.0);
//        AnchorPane.setLeftAnchor(node, 0.0);
//        AnchorPane.setRightAnchor(node, 0.0);

        parentPane.getTabs().add(tab);
        parentPane.getSelectionModel().select(tab);

        init();
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

    private void init(){

        TableColumn<User, String> firstNameColumn = new TableColumn("First name");
        TableColumn<User, String> lastNameColumn = new TableColumn<>("Last name");
        TableColumn<User, String> emailColumn = new TableColumn("Email");
        TableColumn<User, Boolean> defaultColumn = new TableColumn("Is default");
        TableColumn<User, Void> actionColumn = new TableColumn<>("Actions");

        userTable.getColumns().addAll(firstNameColumn, lastNameColumn, emailColumn, defaultColumn, actionColumn);

        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        defaultColumn.setCellValueFactory(cellData -> cellData.getValue().isActiveProperty());

        actionColumn.setCellFactory(addActions());

        userTable.setItems(this.model.getUserList());
    }

    private Callback<TableColumn<User, Void>, TableCell<User, Void>> addActions() {
        return param -> {
            final TableCell<User, Void> cell = new TableCell<User, Void>() {

                Button delete = new Button("");

                Group btnGroup = new Group();

                {

                    delete.setLayoutX(5);

                    btnGroup.getChildren().addAll(delete);
                    FontIcon icon = new FontIcon(FontAwesome.EDIT);

                    delete.setGraphic(new FontIcon(FontAwesome.TRASH));
                    delete.setOnAction(event -> {
                        User user = userTable.getItems().get(getIndex());
                        controller.delete(user);
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
