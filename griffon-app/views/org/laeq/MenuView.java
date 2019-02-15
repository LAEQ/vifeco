package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.laeq.icon.IconService;
import org.laeq.model.Icon;
import org.laeq.model.User;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonView.class)
public class MenuView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private MenuController controller;
    @MVCMember @Nonnull private MenuModel model;

    @FXML private ComboBox<User> userComboBox;
    @FXML private AnchorPane subMenuPane;

    @MVCMember @Nonnull private VifecoView parentView;

    @Inject private IconService iconService;

    @Override
    public void initUI() {
        Node node = loadFromFXML();

        connectActions(node, controller);

        String[] events = new String[]{
                "video.list",
                "video.player",
                "org.laeq.user.list",
                "org.laeq.user.create",
                "mvc.category.list",
                "category.create"
        };

        FontAwesome[] icons = new FontAwesome[]{
                FontAwesome.FILE_MOVIE_O,
                FontAwesome.PLAY_CIRCLE,
                FontAwesome.USERS,
                FontAwesome.USER_PLUS,
                FontAwesome.LIST,
                FontAwesome.PLUS_SQUARE_O
        };

        Map<Group, String> buttons = new HashMap<>();


        parentView.getTop().getChildren().add(node);

        for (int i = 0; i < events.length; i++) {
            Group group = createGroup(icons[i]);

            group.getStyleClass().add("btn-group-vertical");

            group.setLayoutX(50 * i + 10);
            group.setLayoutY(10);

            buttons.put(group, events[i]);

            subMenuPane.getChildren().add(group);

            group.setOnMouseClicked(event -> {
                String eventName = buttons.get(event.getSource());
                publishEvent(eventName);
            });

        }
    }

    private void publishEvent(String eventName){
        getApplication().getEventRouter().publishEvent(eventName);
    }

    private Group createGroup(FontAwesome font){
        Group group = new Group();

        Canvas canvas = new Canvas(40, 40);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf("#DDDDDD"));
        gc.fillRect(0,0,40,40);
        canvas.setOpacity(0.6);

        group.getChildren().add(canvas);

        FontIcon icon = new FontIcon(font);
        icon.setScaleX(1.8);
        icon.setScaleY(1.8);
        icon.setLayoutX(14);
        icon.setLayoutY(24);

//        String path = getApplication().getResourceHandler().getResourceAsURL(pathStr).getPath();
//
//        FileInputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream(path);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        Image image = new Image(inputStream,25, 25, true, true);
//        ImageView imageView = new ImageView(image);
        group.getChildren().add(icon);
//        imageView.setLayoutX(8);
//        imageView.setLayoutY(8);

        return group;
    }

    @FXML
    private void handleUserCombo(){
        controller.setActiveUser(userComboBox.getSelectionModel().getSelectedItem());
    }

    public ComboBox<User> getUserComboBox() {
        return userComboBox;
    }
}
