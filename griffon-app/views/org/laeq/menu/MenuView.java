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
import org.laeq.model.Category;
import org.laeq.model.icon.Color;
import org.laeq.model.icon.IconButton;
import org.laeq.model.icon.IconSVG;
import org.laeq.model.icon.IconSquare;

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

        IconButton videoListBtn = generateButton(IconSVG.video, "video list", "Open the video section", "video.section");
        videoListBtn.setLayoutX(10);
        subMenuPane.getChildren().add(videoListBtn);

        IconButton videoAddBtn = generateButton(IconSVG.video_plus,"video add", "Add a new video", "video.open");
        videoAddBtn.setLayoutX(55);
        subMenuPane.getChildren().add(videoAddBtn);

        IconButton userBtn = generateButton(IconSVG.user, "user", "Open user section", "user.section");
        userBtn.setLayoutX(115);
        subMenuPane.getChildren().add(userBtn);

        IconButton categoryBtn  = generateButton(IconSVG.category, "category", "Open category section", "category.section");
        categoryBtn.setLayoutX(175);
        subMenuPane.getChildren().add(categoryBtn);

        IconButton collectionBtn = generateButton(IconSVG.collection, "collection", "Open collection section", "collection.section");
        collectionBtn.setLayoutX(235);
        subMenuPane.getChildren().add(collectionBtn);

        IconButton databaseBtn = generateButton(IconSVG.db, "database", "Open database section", "database.section");
        databaseBtn.setLayoutX(295);
        subMenuPane.getChildren().add(databaseBtn);

        IconButton importBtn = generateButton(IconSVG.importVideo, "import", "Import a video file", "video.import");
        importBtn.setLayoutX(355);
        subMenuPane.getChildren().add(importBtn);

        IconButton statisticBtn = generateButton(IconSVG.statistic, "statistic", "Open statistic section", "statistic.section");
        statisticBtn.setLayoutX(415);
        subMenuPane.getChildren().add(statisticBtn);
    }

    private IconButton generateButton(String path, String name, String help, String eventName){
        Category category = new Category();
        category.setIcon(path);
        category.setColor(Color.gray_dark);
        IconButton btn = new IconButton(new IconSquare(category), 40);
        btn.decorate();
        Tooltip tooltip = new Tooltip(help);
        Tooltip.install(btn, tooltip);
        btn.setLayoutY(10);

        btn.setOnMouseClicked(event -> {
            getApplication().getEventRouter().publishEvent(eventName);
        });

        return btn;
    }
}
