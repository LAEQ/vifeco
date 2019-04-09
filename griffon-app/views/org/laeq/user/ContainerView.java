package org.laeq.user;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.TranslatedView;
import org.laeq.TranslationService;
import org.laeq.model.Icon;
import org.laeq.model.User;
import org.laeq.model.icon.Color;
import org.laeq.model.icon.IconSVG;
import org.laeq.template.MiddlePaneView;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Locale;

@ArtifactProviderFor(GriffonView.class)
public class ContainerView extends TranslatedView {
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @Inject private TranslationService translationService;

    @FXML private TableView<User> userTable;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;

    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label emailLabel;

    @FXML private Button saveActionTarget;
    @FXML private Button clearActionTarget;

    @Inject private PreferencesService preferencesService;

    @Override
    public void initUI() {
        model.setPrefs(preferencesService.getPreferences());

        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        init();
    }

    private void init(){
        TableColumn<User, String> idColumn = new TableColumn<>("#");
        TableColumn<User, String> firstNameColumn = new TableColumn("");
        TableColumn<User, String> lastNameColumn = new TableColumn<>("");
        TableColumn<User, String> emailColumn = new TableColumn("");
        TableColumn<User, Icon> defaultColumn = new TableColumn("");
        TableColumn<User, Void> actionsColumn = new TableColumn<>("");


        columnsMap.put(firstNameColumn, "org.laeq.user.column.first_name");
        columnsMap.put(lastNameColumn, "org.laeq.user.column.last_name");
        columnsMap.put(emailColumn, "org.laeq.user.column.email");
        columnsMap.put(defaultColumn, "org.laeq.user.column.default");
        columnsMap.put(actionsColumn, "org.laeq.user.column.actions");

        textFields.put(firstNameLabel, "org.laeq.user.first_name");
        textFields.put(lastNameLabel, "org.laeq.user.last_name");
        textFields.put(emailLabel, "org.laeq.user.email");
        textFields.put(saveActionTarget, "org.laeq.user.save_btn");
        textFields.put(clearActionTarget, "org.laeq.user.clear_btn");

        userTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, emailColumn, defaultColumn, actionsColumn);

        idColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> String.valueOf(cellData.getValue().getId())));
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        defaultColumn.setCellValueFactory(cellData -> cellData.getValue().getIsDefault() ? new SimpleObjectProperty<>(new Icon(IconSVG.tick, Color.green)) : null);

        actionsColumn.setCellFactory(addActions());

        model.firstNameProperty().bindBidirectional(firstNameField.textProperty());
        model.lastNameProperty().bindBidirectional(lastNameField.textProperty());
        model.emailProperty().bindBidirectional(emailField.textProperty());

        userTable.setItems(model.getUserList());
        setTranslatedText();
    }

    public void changeLocale() {
        runInsideUISync(() -> {
            setTranslatedText();
        });
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
            getLog().error(e.getMessage());
        }
    }

    private Callback<TableColumn<User, Void>, TableCell<User, Void>> addActions() {
        return param -> {
           final  TableCell<User, Void> cell = new TableCell<User, Void>(){
               Button edit = new Button("");
               Button delete = new Button("");

               Group btnGroup = new Group();
               {
                   btnGroup.getChildren().addAll(edit, delete);
                   edit.setLayoutX(5);
                   delete.setLayoutX(55);

                   Icon icon = new Icon(IconSVG.edit, Color.gray_dark);
                   edit.setGraphic(icon);
                   edit.setOnAction(event -> {
                       model.setSelectedUser(userTable.getItems().get(getIndex()));
                   });

                   delete.setGraphic(new Icon(IconSVG.bin, Color.gray_dark));
                   delete.setOnAction(event -> {
                       controller.delete(userTable.getItems().get(getIndex()));
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
