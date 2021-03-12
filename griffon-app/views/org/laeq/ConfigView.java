package org.laeq;

import griffon.core.Configuration;
import griffon.core.artifact.GriffonView;
import griffon.core.env.Metadata;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Icon;
import org.laeq.model.User;
import org.laeq.model.icon.Color;
import org.laeq.model.icon.IconSVG;
import org.laeq.settings.Settings;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

@ArtifactProviderFor(GriffonView.class)
public class ConfigView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ConfigController controller;
    @MVCMember @Nonnull private ConfigModel model;
    @MVCMember @Nonnull private VifecoView parentView;

    @FXML private ComboBox<String> languages;

    @FXML private Label title;
    @FXML private Label version;
    @FXML private Label buildDate;
    @FXML private Label rootDir;
    @FXML private Label exportDir;
    @FXML private Label javaVersion;
    @FXML private Label fxVersion;
    @FXML private Label notUsed;


    @Inject private PreferencesService preferencesService;

    @Inject private Metadata metadata;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.middle.getChildren().clear();
        parentView.middle.getChildren().add(node);
        connectActions(node, controller);
        connectMessageSource(node);

        title.textProperty().bindBidirectional(model.title);
        version.textProperty().bindBidirectional(model.version);
        buildDate.textProperty().bindBidirectional(model.buildDate);
        rootDir.textProperty().bindBidirectional(model.rootDir);
        exportDir.textProperty().bindBidirectional(model.exportDir);
        javaVersion.textProperty().bindBidirectional(model.version);
        fxVersion.textProperty().bindBidirectional(model.fxVersion);

        init();
    }




    private void init(){
        List<String> langues = new ArrayList<>();
        langues.add("English");
        langues.add("Francais");
        langues.add("Espanol");

        languages.setItems(FXCollections.observableArrayList(langues));
        languages.getSelectionModel().select(0);

        languages.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            controller.setLocale((Integer) newValue);
        });

        model.title.set(metadata.getApplicationName());
        model.version.set(metadata.getApplicationVersion());
        model.buildDate.set(metadata.get("build.date"));
        model.rootDir.set(Settings.defaultPath);
        model.exportDir.set(Settings.exportPath);
        model.javaVersion.set(System.getProperty("java.version"));
        model.fxVersion.set(System.getProperty("javafx.runtime.version"));
    }

    private String translate(String key){
        return getApplication().getMessageSource().getMessage(key);
    }
}
