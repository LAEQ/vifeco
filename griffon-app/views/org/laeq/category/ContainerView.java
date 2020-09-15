package org.laeq.category;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.util.Callback;
import org.laeq.TranslatedView;
import org.laeq.TranslationService;
import org.laeq.model.Category;
import org.laeq.model.Icon;
import org.laeq.model.icon.IconSVG;
import org.laeq.template.MiddlePaneView;
import org.laeq.user.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Locale;

@ArtifactProviderFor(GriffonView.class)
public class ContainerView extends TranslatedView {
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private TextField nameField;
    @FXML private TextField shortCutField;
    @FXML private TextField pathField;
    @FXML private TextField colorPickerField;
    @FXML private Pane svgDisplayPane;

    @FXML private Label titleLabel;
    @FXML private Label nameLabel;
    @FXML private Label shortCutLabel;
    @FXML private Label pathLabel;
    @FXML private Label colorLabel;
    @FXML private Label previewLabel;
    @FXML private Button clearActionTarget;
    @FXML private Button saveActionTarget;


    @FXML private TableView<Category> categoryTable;

    private TranslationService translationService;
    @Inject
    private PreferencesService prefService;

    private SVGPath svgPath;

    @Override
    public void initUI() {
        model.setPrefs(prefService.getPreferences());

        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);

        svgPath = new SVGPath();
        svgPath.setSmooth(true);
        svgPath.setFill(Paint.valueOf("#000000"));
        svgPath.setScaleX(4);
        svgPath.setScaleY(4);
        svgPath.setLayoutX(50);
        svgPath.setLayoutY(50);

        svgDisplayPane.getChildren().add(svgPath);

        init();
        initForm();

        textFields.put(titleLabel, "org.laeq.category.title_create");
        textFields.put(nameLabel, "org.laeq.category.name");
        textFields.put(shortCutLabel, "org.laeq.category.short_cut");
        textFields.put(pathLabel, "org.laeq.category.path");
        textFields.put(colorLabel, "org.laeq.category.color");
        textFields.put(previewLabel, "org.laeq.category.preview");
        textFields.put(clearActionTarget, "org.laeq.category.clear_btn");
        textFields.put(saveActionTarget, "org.laeq.category.save_btn");

        setTranslatedText();
    }

    private void setTranslatedText(){
        try {
            translationService = new TranslationService(getClass().getClassLoader().getResourceAsStream("messages/messages.json"), model.getPrefs().locale);
        } catch (IOException e) {
            getLog().error("Cannot load file messages.json");
        }

        try{
            textFields.entrySet().forEach(t -> {
                t.getKey().setText(translationService.getMessage(t.getValue()));
            });

            columnsMap.entrySet().forEach( t -> {
                t.getKey().setText(translationService.getMessage(t.getValue()));
            });
        } catch (Exception e){
            getLog().error("icit: " + e.getMessage());
        }
    }

    public void initForm(){
        final String[] colorStr = new String[1];

//        colorPickerField.getText()

//        colorPickerField.getText().addListener((observable, oldValue, newValue) -> {
//            Color color = colorPickerField.getValue();
//
//            svgPath.setFill(color);
//            model.setColor(toRGBCode(color));
//        });
    }

    private String toRGBCode(Color color){
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }

    private void init(){
        TableColumn<Category, String> idColumn = new TableColumn<>("#");
        TableColumn<Category, Void> iconColumn = new TableColumn<>("");
        TableColumn<Category, String> nameColumn = new TableColumn("");
        TableColumn<Category, String> shortCutColumn = new TableColumn<>("");
        TableColumn<Category, Void> actionColumn = new TableColumn<>("");

        categoryTable.getColumns().addAll(idColumn, iconColumn, nameColumn, shortCutColumn, actionColumn);

        columnsMap.put(iconColumn, "org.laeq.category.column.icon");
        columnsMap.put(nameColumn, "org.laeq.category.column.name");
        columnsMap.put(shortCutColumn, "org.laeq.category.column.short_cut");
        columnsMap.put(actionColumn, "org.laeq.category.column.actions");

        idColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> String.valueOf(cellData.getValue().getId())));
        nameColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getName()));
        shortCutColumn.setCellValueFactory(param -> Bindings.createStringBinding(() -> param.getValue().getShortcut()));

        model.nameProperty().bindBidirectional(nameField.textProperty());
        model.shortCutProperty().bindBidirectional(shortCutField.textProperty());
        model.iconProperty().bindBidirectional(pathField.textProperty());
        model.colorProperty().bindBidirectional(colorPickerField.textProperty());

        pathField.textProperty().addListener((observable, oldValue, newValue) -> {
           svgPath.setContent(newValue);
        });

        iconColumn.setCellFactory(iconAction());
        actionColumn.setCellFactory(addActions());
        categoryTable.setItems(this.model.getCategoryList());
    }

    private Callback<TableColumn<Category, Void>, TableCell<Category, Void>> addActions() {
        return param -> {
            final  TableCell<Category, Void> cell = new TableCell<Category, Void>(){
                Button edit = new Button("");
                Button delete = new Button("");

                Group btnGroup = new Group();
                {
                    edit.setLayoutX(5);
                    delete.setLayoutX(55);

                    btnGroup.getChildren().addAll(edit, delete);
                    Icon icon = new Icon(IconSVG.edit, org.laeq.model.icon.Color.gray_dark);
                    edit.setGraphic(icon);
                    edit.setOnAction(event -> {
                        model.setSelectedCategory(categoryTable.getItems().get(getIndex()));
                        Category category = categoryTable.getItems().get(getIndex());
                        colorPickerField.setText(category.getColor());
                    });


                    delete.setGraphic(new Icon(IconSVG.bin, org.laeq.model.icon.Color.gray_dark));
                    delete.setOnAction(event -> {
                        controller.delete(categoryTable.getItems().get(getIndex()));
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
    private Callback<TableColumn<Category, Void>, TableCell<Category, Void>> iconAction() {
        return  param -> {
            final TableCell<Category, Void> cell = new TableCell<Category, Void>() {

                SVGPath icon = new SVGPath();

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    try{
                        Category category = categoryTable.getItems().get(getIndex());
                        icon.setContent(category.getIcon());
                        icon.setFill(Paint.valueOf(category.getColor()));

                        colorPickerField.setText(category.getColor());
                    } catch (Exception e){
//                        getLog().error(e.getMessage());
                    }

                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(icon);
                    }
                }
            };

            return cell;
        };
    }

    public void changeLocale(Locale locale) {
        setTranslatedText();
    }
}
