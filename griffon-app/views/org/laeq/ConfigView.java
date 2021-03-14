package org.laeq;


import griffon.core.artifact.GriffonView;
import griffon.core.env.Metadata;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.settings.Settings;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicReference;


@ArtifactProviderFor(GriffonView.class)
public class ConfigView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ConfigController controller;
    @MVCMember @Nonnull private ConfigModel model;
    @MVCMember @Nonnull private VifecoView parentView;

    @FXML private ImageView splash;
    @FXML private ComboBox<String> languages;
    @FXML private Group summary;


    @Inject private PreferencesService preferencesService;

    @Inject private Metadata metadata;

    private Double topText = 280d;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.middle.getChildren().clear();
        parentView.middle.getChildren().add(node);
        connectActions(node, controller);
        connectMessageSource(node);

        init();
    }

    private void positioning(Double value){
        splash.setLayoutX(((Double) value - 128) / 2);
        summary.setLayoutX(((Double) value - 350) / 2);
    }

    private void init(){
        Image image = new Image(getClass().getClassLoader().getResourceAsStream("vifeco_icon/vifeco-icon-128x128.png"));
        splash.setImage(image);
        splash.setPreserveRatio(true);
        splash.setSmooth(true);
        splash.setCache(true);

        parentView.middle.widthProperty().addListener(((observable, oldValue, newValue) -> {
            positioning((Double) newValue);
        }));

        splash.setImage(image);

        model.datas.put("z.title", metadata.getApplicationName());
        model.datas.put("z.version", metadata.getApplicationVersion());
        model.datas.put("z.buildDate",  metadata.get("build.date"));
        model.datas.put("z.rootDir",  Settings.defaultPath);
        model.datas.put("z.dbDir", Settings.dbPath);
        model.datas.put("z.exportDir",  Settings.exportPath);
        model.datas.put("z.javaVersion", System.getProperty("java.version"));
        model.datas.put("z.fxVersion", System.getProperty("javafx.runtime.version"));

        AtomicReference<Double> xPos = new AtomicReference<>(0d);

        model.datas.forEach((k, v) -> {
            if(k.equals("z.title")){
                return;
            }
            Label label = new Label();
            label.setText(String.format("%s : %s\n", translate(k), v));
            label.setMinWidth(350);
            label.setMaxWidth(350);
            label.setLayoutY(topText);
            label.setLayoutX(0);
            label.setAlignment(Pos.TOP_CENTER);
            label.setLayoutY(xPos.get());
            xPos.updateAndGet(v1 -> v1 + 25);
            summary.getChildren().add(label);
        });

        positioning(parentView.middle.getWidth());
    }

    private String translate(String key){
        return getApplication().getMessageSource().getMessage(key);
    }

    public void setLocale(int localeIndex) {
        languages.getSelectionModel().select(localeIndex);

        languages.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            controller.setLocale((Integer) newValue);
        });
    }
}
