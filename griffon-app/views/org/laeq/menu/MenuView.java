package org.laeq.menu;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import org.laeq.TranslatedView;
import org.laeq.TranslationService;
import org.laeq.VifecoView;
import org.laeq.icon.IconService;
import org.laeq.model.Category;
import org.laeq.model.icon.Color;
import org.laeq.model.icon.IconButton;
import org.laeq.model.icon.IconSVG;
import org.laeq.model.icon.IconSquare;
import org.laeq.user.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ArtifactProviderFor(GriffonView.class)
public class MenuView extends TranslatedView {
    @MVCMember @Nonnull private MenuController controller;
    @MVCMember @Nonnull private MenuModel model;

    @FXML private AnchorPane subMenuPane;
    @FXML private ChoiceBox<String> languageMenu;

    @MVCMember @Nonnull private VifecoView parentView;

    @Inject private IconService iconService;

    @Inject private PreferencesService prefService;

    private final Map<IconButton, String> btnTooltipMessages = new HashMap<>();
    private final Map<IconButton, Tooltip> toolTips = new HashMap<>();

    private TranslationService translationService;

    @Override
    public void initUI() {

        model.setPrefs(prefService.getPreferences());

        try {
            translationService = new TranslationService(getClass().getClassLoader().getResourceAsStream("messages/messages.json"), model.getPrefs().locale);
        } catch (IOException e) {
            getLog().error("Cannot load file messages.json");
        }


        Node node = loadFromFXML();
        connectActions(node, controller);

        parentView.getTop().getChildren().add(node);

        IconButton videoListBtn = generateButton(IconSVG.video, "video list", "org.laeq.menu.tooltip.video_list", "video.section");
        videoListBtn.setLayoutX(10);
        subMenuPane.getChildren().add(videoListBtn);

        IconButton videoAddBtn = generateButton(IconSVG.video_plus,"video add", "org.laeq.menu.tooltip.video_add", "video.open");
        videoAddBtn.setLayoutX(55);
        subMenuPane.getChildren().add(videoAddBtn);

        IconButton userBtn = generateButton(IconSVG.user, "user", "org.laeq.menu.tooltip.user", "user.section");
        userBtn.setLayoutX(115);
        subMenuPane.getChildren().add(userBtn);

        IconButton categoryBtn  = generateButton(IconSVG.category, "category", "org.laeq.menu.tooltip.category", "category.section");
        categoryBtn.setLayoutX(175);
        subMenuPane.getChildren().add(categoryBtn);

        IconButton collectionBtn = generateButton(IconSVG.collection, "collection", "org.laeq.menu.tooltip.collection", "collection.section");
        collectionBtn.setLayoutX(235);
        subMenuPane.getChildren().add(collectionBtn);

        IconButton databaseBtn = generateButton(IconSVG.db, "database", "org.laeq.menu.tooltip.database", "database.section");
        databaseBtn.setLayoutX(295);
        subMenuPane.getChildren().add(databaseBtn);

        IconButton importBtn = generateButton(IconSVG.importVideo, "import", "org.laeq.menu.tooltip.import_video", "video.import");
        importBtn.setLayoutX(355);
        subMenuPane.getChildren().add(importBtn);

        IconButton statisticBtn = generateButton(IconSVG.statistic, "statistic", "org.laeq.menu.tooltip.statistic", "statistic.section");
        statisticBtn.setLayoutX(415);
        subMenuPane.getChildren().add(statisticBtn);

        languageMenu.setItems(FXCollections.observableArrayList(model.getPrefs().getLocales()));

        languageMenu.getSelectionModel().select(model.getPrefs().getLocalIndex());

        languageMenu.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            model.getPrefs().setLocaleByIndex(newValue.intValue());
            runOutsideUI(() -> prefService.export(model.getPrefs()));

            controller.changeLanguage();
        });
    }

    private IconButton generateButton(String path, String name, String help, String eventName){
        Category category = new Category();
        category.setIcon(path);
        category.setColor(Color.gray_dark);
        IconButton btn = new IconButton(new IconSquare(category), 40);
        btn.decorate();

        btnTooltipMessages.put(btn, help);

//        btn.setOnMouseEntered(event -> Tooltip.install((Node) event.getSource(), generateToolTip((IconButton) event.getSource())));
//        btn.setOnMouseExited(event -> Tooltip.uninstall((Node) event.getSource(), generateToolTip((IconButton) event.getSource())));

        btn.setOnMouseClicked(event -> getApplication().getEventRouter().publishEvent(eventName));

        btn.setLayoutY(10);

        return btn;
    }

    private Tooltip generateToolTip(IconButton btn){
        if(! toolTips.containsValue(btn)){
            toolTips.put(btn, new Tooltip(translationService.getMessage(btnTooltipMessages.get(btn))));
        }

        return toolTips.get(btn);
    }

    public void updateTranslation() {
//        toolTips.clear();
        
    }
}
