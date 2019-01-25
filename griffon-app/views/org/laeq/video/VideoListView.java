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
import org.laeq.model.Person;

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

    @FXML private TableView<Person> videoListTable;

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

        final ObservableList<Person> data = FXCollections.observableArrayList(
                new Person("Jacob", "Smith", "jacob.smith@example.com"),
                new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
                new Person("Ethan", "Williams", "ethan.williams@example.com"),
                new Person("Emma", "Jones", "emma.jones@example.com"),
                new Person("Michael", "Brown", "michael.brown@example.com")
        );

        firstNameCol.setCellValueFactory(new PropertyValueFactory<
                Person, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<
                Person, String>("lastName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<
                Person, String>("email"));

        videoListTable.setItems(data);
//        videoListPane.getChildren().add(videoListTable);

        parentView.getMiddlePane().getItems().add(node);
    }

}
