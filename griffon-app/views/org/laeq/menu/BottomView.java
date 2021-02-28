package org.laeq.menu;

import griffon.core.artifact.GriffonView;
import griffon.core.i18n.MessageSource;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;

import javax.annotation.Nonnull;
import java.util.List;

import static java.util.Arrays.asList;

@ArtifactProviderFor(GriffonView.class)
public class BottomView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private BottomController controller;
    @MVCMember @Nonnull private BottomModel model;

    @MVCMember @Nonnull private VifecoView parentView;

    @FXML private Label message;
    @FXML private Pane box;

    @Override
    public void initUI() {
//        messageSource = getApplication().getMessageSource();
        Node node = loadFromFXML();
        connectActions(node, controller);

        parentView.bottom.getChildren().add(node);

        System.out.println(model.message.toString());
        model.message.bindBidirectional(message.textProperty());
        model.styles.addListener((ListChangeListener<String>) c -> {
            box.getStylesheets().clear();
            box.getStylesheets().setAll("alert", "alert-success");
        });
    }


    public void setMessageParametized(Object[] objects, List<String> styles) {
//        String key = (String) objects[0];
//        String param = (String) objects[1];
//        String text = messageSource.getMessage(key, asList(param));
//        panel.getStyleClass().setAll(styles);
//        label.setText(text);
    }
}
