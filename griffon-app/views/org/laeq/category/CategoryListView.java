package org.laeq.category;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.laeq.icon.IconService;
import org.laeq.model.Category;
import org.laeq.model.Icon;
import org.laeq.model.User;
import org.laeq.video.VideoListView;

import java.io.FileNotFoundException;
import java.util.Collections;
import javax.annotation.Nonnull;
import javax.inject.Inject;

@ArtifactProviderFor(GriffonView.class)
public class CategoryListView extends AbstractJavaFXGriffonView {
    private TabPane parentPane;
    private Tab tab;

    @MVCMember @Nonnull private CategoryListController controller;
    @MVCMember @Nonnull private CategoryListModel model;
    @MVCMember @Nonnull private VideoListView parentView;

    @FXML private TableView<Category> categoryTable;

    @Inject private IconService iconService;

    @Override
    public void initUI() {
        parentPane = parentView.getTabPane();
        Node node = loadFromFXML();

        tab = new Tab();
        tab.setGraphic(new FontIcon(FontAwesome.LIST));
        tab.setText("Category list");
        tab.setId("category_list");
        tab.setContent(node);
        tab.setClosable(true);

        tab.setOnClosed(closeTab());

        parentPane.getTabs().add(tab);
        parentPane.getSelectionModel().select(tab);

        connectActions(node, controller);

//        init();
    }

    @Override
    public void mvcGroupDestroy(){
        runInsideUISync(()-> {
            parentPane.getTabs().remove(tab);
        });
    }

    public void init(){
        TableColumn<Category, Void> iconColumn = new TableColumn<>("Icon");
        TableColumn<Category, String> nameColumn = new TableColumn("Name");
        TableColumn<Category, String> shortCut = new TableColumn<>("Shortcut");
        TableColumn<Category, String> pathColumn = new TableColumn("path");
        TableColumn<Category, Void> actionColumn = new TableColumn<>("Actions");

        categoryTable.getColumns().addAll(iconColumn, nameColumn, shortCut, pathColumn, actionColumn);

        nameColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getName()));
        shortCut.setCellValueFactory(param -> Bindings.createStringBinding(() -> param.getValue().getShortcut()));
        pathColumn.setCellValueFactory(param -> Bindings.createStringBinding(() -> param.getValue().getIcon()));

        iconColumn.setCellFactory(iconAction());
        actionColumn.setCellFactory(addActions());

        categoryTable.setItems(this.model.getCategoryList());
    }

    private Callback<TableColumn<Category, Void>, TableCell<Category, Void>> iconAction() {
        return  param -> {
            final TableCell<Category, Void> cell = new TableCell<Category, Void>() {

                Icon icon = iconService.generateRandomIcon();

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    try{
                        Category category = categoryTable.getItems().get(getIndex());
                        icon = iconService.generateIcon(category);
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }

                    if (empty) {
                        System.out.println("empty");
                        setGraphic(null);
                    } else {
                        System.out.println("icon");
                        setGraphic(icon);
                    }
                }
            };

            return cell;
        };
    }

    private Callback<TableColumn<Category, Void>, TableCell<Category, Void>> addActions() {
        return param -> {
            final TableCell<Category, Void> cell = new TableCell<Category, Void>() {

                Button delete = new Button("");
                Group btnGroup = new Group();
                {
                    delete.setLayoutX(5);
                    btnGroup.getChildren().addAll(delete);
                    FontIcon icon = new FontIcon(FontAwesome.TRASH);
                    delete.setGraphic(new FontIcon(FontAwesome.TRASH));
                    delete.setOnAction(event -> {
                        Category category = categoryTable.getItems().get(getIndex());
                        controller.delete(category);
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

    private EventHandler<Event> closeTab(){
        return event -> controller.closeTab();
    }

}
