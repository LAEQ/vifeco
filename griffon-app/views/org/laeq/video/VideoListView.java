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
import javafx.scene.layout.AnchorPane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import org.laeq.model.Video;
import org.laeq.model.VideoUser;

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

    @FXML private TableView<VideoUser> videoListTable;

    @FXML private TabPane tabPane;

    @Nonnull
    public TabPane getTabPane() {
        return tabPane;
    }

    private ObservableList<VideoUser> videoList;

    @Override
    public void initUI() {
        Node node = loadFromFXML();

        videoList = FXCollections.observableList(this.model.getVideoList());

        TableColumn<VideoUser, String> firstNameColumn = new TableColumn("Video path");
        TableColumn<VideoUser, String> durationColumne = new TableColumn("Duration");

        videoListTable.getColumns().addAll(firstNameColumn, durationColumne);
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().getVideo().pathProperty());
        durationColumne.setCellValueFactory(cellData -> cellData.getValue().getVideo().durationProperty().asString());

        videoListTable.setItems(videoList);
        parentView.getMiddlePane().getItems().add(node);
    }

    public void addVideo(VideoUser video){
        videoList.add(video);
    }

}
