package org.laeq;


import griffon.core.artifact.GriffonView;
import griffon.core.env.Metadata;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.settings.Settings;
import javax.annotation.Nonnull;
import javax.inject.Inject;



@ArtifactProviderFor(GriffonView.class)
public class ConfigView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ConfigController controller;
    @MVCMember @Nonnull private ConfigModel model;
    @MVCMember @Nonnull private VifecoView parentView;

    @FXML private ImageView splash;
    @FXML private ComboBox<String> languages;
    @FXML private TextArea summary;


    @Inject private PreferencesService preferencesService;

    @Inject private Metadata metadata;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.middle.getChildren().clear();
        parentView.middle.getChildren().add(node);
        connectActions(node, controller);
        connectMessageSource(node);

        init();
    }

    private void init(){
        languages.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            controller.setLocale((Integer) newValue);
        });

//        Image image = new Image(getClass().getClassLoader().getResourceAsStream("splash.png"));
//        ImageView imageView = new ImageView();
//        imageView.setImage(image);
//        imageView.setPreserveRatio(true);
//        imageView.setSmooth(true);
//        imageView.setCache(true);
//        splash.setPreserveRatio(true);
//        splash.fitWidthProperty().bind(parentView.middle.widthProperty());
//        splash.setImage(image);


        model.datas.put("z.title", metadata.getApplicationName());
        model.datas.put("z.version", metadata.getApplicationVersion());
        model.datas.put("z.buildDate",  metadata.get("build.date"));
        model.datas.put("z.rootDir",  Settings.defaultPath);
        model.datas.put("z.dbDir", Settings.dbPath);
        model.datas.put("z.exportDir",  Settings.exportPath);
        model.datas.put("z.javaVersion", System.getProperty("java.version"));
        model.datas.put("z.fxVersion", System.getProperty("javafx.runtime.version"));

        StringBuilder builder = new StringBuilder();

        model.datas.forEach((k, v) -> {
            builder.append(String.format("%20s : %s\n", translate(k).trim(), v.trim()));
        });

        summary.setText(builder.toString());

    }

    private String translate(String key){
        return getApplication().getMessageSource().getMessage(key);
    }

    public void setLocale(int localeIndex) {
        languages.getSelectionModel().select(localeIndex);
    }
}
