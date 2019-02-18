package org.laeq.collection;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.CategoryCollection;
import org.laeq.template.MiddlePaneView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class ContainerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private ContainerModel model;

    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private TableView<CategoryCollection> collectionTable;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        init();
    }

    private void init(){
        TableColumn<CategoryCollection, String> nameColumn = new TableColumn("Name");

        collectionTable.getColumns().addAll(nameColumn);
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        collectionTable.setItems(this.model.getCollections());
    }
}
