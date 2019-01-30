package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import org.laeq.model.User;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class VideoListView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull
    private VideoListController controller;

    @MVCMember @Nonnull
    private VideoListModel model;

    @MVCMember @Nonnull
    private VifecoView parentView;

    @FXML private AnchorPane videoListPane;

    @FXML private TableView<User> videoListTable;

    @FXML private TabPane tabPane;

    @Nonnull
    public TabPane getTabPane() {
        return tabPane;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();

        TableColumn firstNameCol = new TableColumn("First Name");
        TableColumn lastNameCol = new TableColumn("Last Name");
        TableColumn emailCol = new TableColumn("Email");

        videoListTable.getColumns().addAll(firstNameCol, lastNameCol, emailCol);

        final ObservableList<User> data = FXCollections.observableArrayList(
                new User("Jacob", "Smith", "jacob.smith@example.com"),
                new User("Isabella", "Johnson", "isabella.johnson@example.com"),
                new User("Ethan", "Williams", "ethan.williams@example.com"),
                new User("Emma", "Jones", "emma.jones@example.com"),
                new User("Michael", "Brown", "michael.brown@example.com")
        );

        firstNameCol.setCellValueFactory(new PropertyValueFactory<
                User, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<
                User, String>("lastName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<
                User, String>("email"));

        videoListTable.setItems(data);
//        videoListPane.getChildren().add(videoListTable);

        parentView.getMiddlePane().getItems().add(node);
    }

}
