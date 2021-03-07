package org.laeq.tools;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.CategoryController;
import org.laeq.CategoryModel;
import org.laeq.VifecoView;
import org.laeq.model.Category;

import javax.annotation.Nonnull;
import java.util.Arrays;

@ArtifactProviderFor(GriffonView.class)
public class ImportView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ImportController controller;
    @MVCMember @Nonnull private ImportModel model;
    @MVCMember @Nonnull private VifecoView parentView;

    @FXML private Label filename;
    @FXML private TextArea warning;


//    @FXML private TableView<Category> categoryTable;
//    @FXML private TableColumn<Category, String> id;
//    @FXML private TableColumn<Category, String> name;


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
       model.warning.bindBidirectional(warning.textProperty());

    }

    private String translate(String key){
        return getApplication().getMessageSource().getMessage(key);
    }
}
