package org.laeq.menu;

import griffon.core.artifact.GriffonView;
import griffon.core.i18n.MessageSource;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class BottomView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private BottomController controller;
    @MVCMember @Nonnull private BottomModel model;

    @MVCMember @Nonnull private VifecoView parentView;

    @FXML private Label label;

    private MessageSource messageSource;

    @Override
    public void initUI() {
        messageSource = getApplication().getMessageSource();

        Node node = loadFromFXML();

        connectActions(node, controller);

        parentView.getBottom().getChildren().add(node);

        label.setText("FDFSDF");
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void setSuccessMessage(String message) {
        String text = messageSource.getMessage(message);
        System.out.println(text);
        label.setText(text);
    }
}
