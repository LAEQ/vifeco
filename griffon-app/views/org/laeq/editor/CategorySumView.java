package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.core.i18n.MessageSource;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.HelperService;
import org.laeq.model.CategoryCount;
import org.laeq.model.Icon;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.comparator.DurationComparator;

import javax.annotation.Nonnull;
import javax.inject.Inject;

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
