package org.laeq.menu;

import griffon.core.artifact.GriffonView;
import griffon.core.i18n.MessageSource;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;

import javax.annotation.Nonnull;

import static java.util.Arrays.asList;

@ArtifactProviderFor(GriffonView.class)
public class BottomView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private BottomController controller;
    @MVCMember @Nonnull private BottomModel model;

    @MVCMember @Nonnull private VifecoView parentView;

    @FXML private Label label;
    @FXML private Pane panel;

    private MessageSource messageSource;

    @Override
    public void initUI() {
        messageSource = getApplication().getMessageSource();

        Node node = loadFromFXML();

        connectActions(node, controller);

        parentView.getBottom().getChildren().add(node);

        label.setText("");
    }

    public void setSuccessMessage(String message) {
        String text = messageSource.getMessage(message);
        panel.getStyleClass().setAll("alert", "alert-success");
        label.setText(text);
    }

    public void setErrorMessage(String message) {
        String text = messageSource.getMessage(message);
        panel.getStyleClass().setAll("alert", "alert-danger");
        label.setText(text);
    }

    public void setInfo(String message) {
        String text = messageSource.getMessage(message);
        panel.getStyleClass().setAll("alert", "alert-info");
        label.setText(text);
    }

    public void setInfoParametized(Object[] objects) {
        String key = (String) objects[0];
        String param = (String) objects[1];
        String text = messageSource.getMessage(key, asList(param));
        panel.getStyleClass().setAll("alert", "alert-info");
        label.setText(text);
    }
}
