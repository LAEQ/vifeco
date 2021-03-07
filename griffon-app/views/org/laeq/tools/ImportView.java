package org.laeq.tools;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import org.laeq.model.Collection;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class ImportView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ImportController controller;
    @MVCMember @Nonnull private ImportModel model;
    @MVCMember @Nonnull private VifecoView parentView;

    @FXML private Label filename;
    @FXML private TextArea report;
    @FXML private Label warning;


    @FXML private TableView<Collection> collectionTable;
    @FXML private TableColumn<Collection, Integer> id;
    @FXML private TableColumn<Collection, String> name;
    @FXML private TableColumn<Collection, String> categories;


    @Override
    public void initUI() {
        Node node = loadFromFXML();

        parentView.middle.getChildren().clear();
        parentView.middle.getChildren().add(node);
        connectMessageSource(node);
        connectActions(node, controller);

        init();
    }

    private void init(){
        model.filename.bindBidirectional(filename.textProperty());
        model.report.bindBidirectional(report.textProperty());
        model.warning.bindBidirectional(warning.textProperty());

        id.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        categories.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCategorieNamesAndIds()));

        collectionTable.setItems(this.model.collections);

        model.styles.addListener((ListChangeListener<String>) c -> {
            warning.getStyleClass().clear();
            warning.getStyleClass().setAll(c.getList());
        });
    }

    private String translate(String key){
        return getApplication().getMessageSource().getMessage(key);
    }
}
