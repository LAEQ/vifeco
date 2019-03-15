package org.laeq.menu;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import org.laeq.icon.IconService;
import org.laeq.model.icon.Color;
import org.laeq.model.icon.IconSVG;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@ArtifactProviderFor(GriffonView.class)
public class MenuView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private MenuController controller;
    @MVCMember @Nonnull private MenuModel model;

    @FXML private AnchorPane subMenuPane;

    @MVCMember @Nonnull private VifecoView parentView;

    @Inject private IconService iconService;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        connectActions(node, controller);

        parentView.getTop().getChildren().add(node);

        Button videoListBtn = generateButton(IconSVG.video, "video list", "Open the video section", "video.section");
        videoListBtn.setLayoutX(10);
        subMenuPane.getChildren().add(videoListBtn);

        Button videoAddBtn = generateButton(IconSVG.video_plus,"video add", "Add a new video", "video.open");
        videoAddBtn.setLayoutX(55);
        subMenuPane.getChildren().add(videoAddBtn);

        Button userBtn = generateButton(IconSVG.user, "user", "Open user section", "user.section");
        userBtn.setLayoutX(115);
        subMenuPane.getChildren().add(userBtn);

        Button categoryBtn  = generateButton(IconSVG.category, "category", "Open category section", "category.section");
        categoryBtn.setLayoutX(175);
        subMenuPane.getChildren().add(categoryBtn);

        Button collectionBtn = generateButton(IconSVG.collection, "collection", "Open collection section", "collection.section");
        collectionBtn.setLayoutX(235);
        subMenuPane.getChildren().add(collectionBtn);

        Button databaseBtn = generateButton(IconSVG.db, "database", "Open database section", "database.section");
        databaseBtn.setLayoutX(295);
        subMenuPane.getChildren().add(databaseBtn);

        Button statisticBtn = generateButton(IconSVG.statistic, "statistic", "Open statistic section", "statistic.section");
        statisticBtn.setLayoutX(355);
        subMenuPane.getChildren().add(statisticBtn);
    }

    private Button generateButton(String path, String name, String help, String eventName){
        Button btn = new Button(path, Color.gray_dark, name, help, eventName);
        Tooltip tooltip = new Tooltip(help);
        Tooltip.install(btn, tooltip);
        btn.setLayoutY(10);

        btn.setOnMouseClicked(event -> {
            getApplication().getEventRouter().publishEvent(eventName);
        });

        return btn;
    }

    private void publishEvent(String eventName){
        getApplication().getEventRouter().publishEvent(eventName);
    }
}
