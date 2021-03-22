package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.CategoryCount;
import org.laeq.model.Icon;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class CategorySumView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private CategorySumModel model;
    @MVCMember @Nonnull private PlayerView parentView;

    @FXML public TableView<CategoryCount> summaryTable;
    @FXML public TableColumn<CategoryCount, Icon> iconTS;
    @FXML public TableColumn<CategoryCount, String> nameTS;
    @FXML public TableColumn<CategoryCount, String> shortcutTS;
    @FXML public TableColumn<CategoryCount, Number> totalTS;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.summary.getChildren().add(node);
        connectMessageSource(node);

        iconTS.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().category.getIcon2()));
        nameTS.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().category.getName()));
        shortcutTS.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().category.getShortcut()));
        totalTS.setCellValueFactory(cellData -> cellData.getValue().total);
        summaryTable.setItems(model.summary);
    }
}
