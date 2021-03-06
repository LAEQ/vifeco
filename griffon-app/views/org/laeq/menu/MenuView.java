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
import org.laeq.model.Category;
import org.laeq.model.icon.Color;
import org.laeq.model.icon.IconButton;
import org.laeq.model.icon.IconSVG;
import org.laeq.model.icon.IconSquare;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonView.class)
public class MenuView extends AbstractJavaFXGriffonView {
    private MenuController controller;
    private MenuModel model;

    @FXML private AnchorPane subMenuPane;

    @MVCMember @Nonnull private VifecoView parentView;

    @MVCMember
    public void setModel(@Nonnull MenuModel model){
        this.model = model;
    }

    @MVCMember
    public void setController(@Nonnull MenuController controller){
        this.controller = controller;
    }

    private final Map<IconButton, String> btnTooltipMessages = new HashMap<>();
    private final Map<IconButton, Tooltip> toolTips = new HashMap<>();


    @Override
    public void initUI() {
        Node node = loadFromFXML();
        connectActions(node, controller);

        parentView.menu.getChildren().add(node);

        IconButton videoAddBtn = generateButton(IconSVG.video_plus,"video add", "org.laeq.menu.tooltip.video_add", "video.open");
        videoAddBtn.setLayoutX(10);
        subMenuPane.getChildren().add(videoAddBtn);

        IconButton videoListBtn = generateButton(IconSVG.video, "video list", "org.laeq.menu.tooltip.video_list", "video.section");
        videoListBtn.setLayoutX(65);
        subMenuPane.getChildren().add(videoListBtn);

        IconButton userBtn = generateButton(IconSVG.user, "user", "org.laeq.menu.tooltip.user", "user.section");
        userBtn.setLayoutX(110);
        subMenuPane.getChildren().add(userBtn);

        IconButton categoryBtn  = generateButton(IconSVG.category, "category", "org.laeq.menu.tooltip.category", "category.section");
        categoryBtn.setLayoutX(155);
        subMenuPane.getChildren().add(categoryBtn);

        IconButton collectionBtn = generateButton(IconSVG.collection, "collection", "org.laeq.menu.tooltip.collection", "collection.section");
        collectionBtn.setLayoutX(200);
        subMenuPane.getChildren().add(collectionBtn);

        IconButton databaseBtn = generateButton(IconSVG.db, "database", "org.laeq.menu.tooltip.database", "database.backup");
        databaseBtn.setLayoutX(255);
        subMenuPane.getChildren().add(databaseBtn);

        IconButton importBtn = generateButton(IconSVG.importVideo, "import", "org.laeq.menu.tooltip.import_video", "video.import");
        importBtn.setLayoutX(300);
        subMenuPane.getChildren().add(importBtn);

        IconButton statisticBtn = generateButton(IconSVG.statistic, "statistic", "org.laeq.menu.tooltip.statistic", "statistic.section");
        statisticBtn.setLayoutX(345);
        subMenuPane.getChildren().add(statisticBtn);

        IconButton aboutBtn = generateButton(IconSVG.question, "about", "org.laeq.menu.tooltip.about", "about.section");
        aboutBtn.setLayoutX(390);
        subMenuPane.getChildren().add(aboutBtn);

        IconButton configBtn = generateButton(IconSVG.cog, "config", "org.laeq.menu.tooltip.config", "config.section");
        configBtn.setLayoutX(435);
        subMenuPane.getChildren().add(configBtn);
    }

    private IconButton generateButton(String path, String name, String help, String eventName){
        Category category = new Category();
        category.setIcon(path);
        category.setColor(Color.gray_dark);
        IconButton btn = new IconButton(new IconSquare(category), 40);
        btn.decorate();

        btnTooltipMessages.put(btn, help);

        btn.setOnMouseClicked(event -> getApplication().getEventRouter().publishEvent(eventName));

        btn.setLayoutY(10);

        return btn;
    }
}
