package org.laeq;

import com.pepperonas.fxiconics.FxIconicsButton;
import com.pepperonas.fxiconics.FxIconicsLabel;
import com.pepperonas.fxiconics.MaterialColor;
import com.pepperonas.fxiconics.cmd.FxFontCommunity;
import com.pepperonas.fxiconics.gmd.FxFontGoogleMaterial;
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.stage.PopupWindow;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.laeq.graphic.IconSVG;
import org.laeq.icon.IconService;
import org.laeq.menu.Button;
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
                "category.create",
                "category_collection.create"
        };

        FontAwesome[] icons = new FontAwesome[]{
                FontAwesome.FILE_MOVIE_O,
                FontAwesome.PLAY_CIRCLE,
                FontAwesome.USERS,
                FontAwesome.USER_PLUS,
                FontAwesome.LIST,
                FontAwesome.PLUS_SQUARE_O,
                FontAwesome.PLUS_CIRCLE,
                FontAwesome.FILE
        };

        Map<Group, String> buttons = new HashMap<>();

        parentView.getTop().getChildren().add(node);

        Button videoListBtn = generateButton(IconSVG.video, "video list", "Open the video section", "video.section");
        videoListBtn.setLayoutX(10);
        subMenuPane.getChildren().add(videoListBtn);

        Button videoAddBtn = generateButton(IconSVG.video_plus,"video add", "Add a new video", "video.add");
        videoAddBtn.setLayoutX(60);
        subMenuPane.getChildren().add(videoAddBtn);

        Button userBtn = generateButton(IconSVG.user, "user", "Open user section", "user.section");
        userBtn.setLayoutX(120);
        subMenuPane.getChildren().add(userBtn);

        Button categoryBtn  = generateButton(IconSVG.category, "category", "Open category section", "category.section");
        categoryBtn.setLayoutX(180);
        subMenuPane.getChildren().add(categoryBtn);

        Button collectionBtn = generateButton(IconSVG.collection, "collection", "Open collection section", "collection.section");
        collectionBtn.setLayoutX(230);
        subMenuPane.getChildren().add(collectionBtn);

        Button statisticBtn = generateButton(IconSVG.statistic, "statistic", "Open statistic section", "statistic.section");
        statisticBtn.setLayoutX(290);
        subMenuPane.getChildren().add(statisticBtn);



//        for (int i = 0; i < events.length; i++) {
//            Group group = createGroup(icons[i]);
//
//            group.getStyleClass().add("btn-group-vertical");
//
//            group.setLayoutX(50 * i + 10);
//            group.setLayoutY(10);
//
//            buttons.put(group, events[i]);
//
////            subMenuPane.getChildren().add(group);
//
//            group.setOnMouseClicked(event -> {
//                String eventName = buttons.get(event.getSource());
//                publishEvent(eventName);
//            });
//        }
    }

    private Button generateButton(String path, String name, String help, String eventName){
        Button btn = new Button(path, org.laeq.graphic.Color.gray_dark, name, help, eventName);
        Tooltip tooltip = new Tooltip(help);
        Tooltip.install(btn, tooltip);
        btn.setLayoutY(10);

        btn.setOnMouseClicked(event -> {
            getApplication().getEventRouter().publishEventAsync(eventName);
        });

        return btn;
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

        SVGPath svg = new SVGPath();
//        svg.setCon
        svg.setSmooth(true);
        svg.setFill(Paint.valueOf("#FF0000"));
        svg.setContent("M18 15.422v.983c0 .771-1.862 1.396-4 1.396s-4-.625-4-1.396v-.983c.968.695 2.801.902 4 .902 1.202 0 3.035-.208 4-.902zm-4-1.363c-1.202 0-3.035-.209-4-.902v.973c0 .771 1.862 1.396 4 1.396s4-.625 4-1.396v-.973c-.968.695-2.801.902-4 .902zm0-5.86c-2.138 0-4 .625-4 1.396 0 .77 1.862 1.395 4 1.395s4-.625 4-1.395c0-.771-1.862-1.396-4-1.396zm0 3.591c-1.202 0-3.035-.209-4-.902v.977c0 .77 1.862 1.395 4 1.395s4-.625 4-1.395v-.977c-.968.695-2.801.902-4 .902zm-.5-9.79c-5.288 0-9.649 3.914-10.377 9h-3.123l4 5.917 4-5.917h-2.847c.711-3.972 4.174-7 8.347-7 4.687 0 8.5 3.813 8.5 8.5s-3.813 8.5-8.5 8.5c-3.015 0-5.662-1.583-7.171-3.957l-1.2 1.775c1.916 2.536 4.948 4.182 8.371 4.182 5.797 0 10.5-4.702 10.5-10.5s-4.703-10.5-10.5-10.5z");
//        svg.setContent("M9.5 10.287c0-.41-.336-.743-.75-.743s-.75.333-.75.743.336.743.75.743.75-.333.75-.743zm4.5.495c0-.137-.112-.248-.25-.248h-3.5c-.138 0-.25.111-.25.248s.112.248.25.248h3.5c.138-.001.25-.112.25-.248zm2-.495c0-.41-.336-.743-.75-.743s-.75.333-.75.743.336.743.75.743.75-.333.75-.743zm-8.649-3.219h-1.101c-.138 0-.25.111-.25.248v.253c0 .393.463.49.808.49l.543-.991zm9.659 1.569c-.435-.8-.866-1.597-1.342-2.382-.393-.649-.685-.96-1.375-1.083-.698-.124-1.341-.172-2.293-.172s-1.595.048-2.292.172c-.69.123-.982.433-1.375 1.083-.477.785-.907 1.582-1.343 2.382-.344.63-.49 1.194-.49 1.884 0 .653.21 1.195.5 1.89v1.094c0 .273.224.495.5.495h.75c.276 0 .5-.222.5-.495v-.495h6.5v.495c0 .273.224.495.5.495h.75c.276 0 .5-.222.5-.495v-1.094c.29-.695.5-1.237.5-1.89 0-.69-.146-1.254-.49-1.884zm-7.821-1.873c.335-.554.426-.569.695-.617.635-.113 1.228-.157 2.116-.157s1.481.044 2.116.156c.269.048.36.064.695.617.204.337.405.687.597 1.03-.728.11-2.01.266-3.408.266-1.524 0-2.759-.166-3.402-.275.19-.34.389-.686.591-1.02zm5.798 5.256h-5.974c-.836 0-1.513-.671-1.513-1.498 0-.813.253-1.199.592-1.821.52.101 1.984.348 3.908.348 1.74 0 3.28-.225 3.917-.333.332.609.583.995.583 1.805 0 .828-.677 1.499-1.513 1.499zm2.763-4.952c.138 0 .25.111.25.248v.253c0 .393-.463.49-.808.49l-.543-.99h1.101zm-5.75-7.068c-5.523 0-10 4.394-10 9.815 0 5.505 4.375 9.268 10 14.185 5.625-4.917 10-8.68 10-14.185 0-5.421-4.478-9.815-10-9.815zm0 18c-4.419 0-8-3.582-8-8s3.581-8 8-8c4.419 0 8 3.582 8 8s-3.581 8-8 8z");
        svg.setScaleX(1.2);
        svg.setScaleY(1.2);
        svg.setLayoutX(7);
        svg.setLayoutY(7);
        group.getChildren().add(svg);
//        group.getChildren().add(icon);

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
//        group.getChildren().add(imageView);
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
