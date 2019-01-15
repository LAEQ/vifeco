package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import java.util.Collections;
import java.util.Map;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class TopMenuView extends AbstractJavaFXGriffonView {
    @FXML
    private AnchorPane menu1;

    @FXML
    private AnchorPane menu2;

    @Nonnull
    public AnchorPane getMenu1() {
        return menu1;
    }

    @Nonnull
    public AnchorPane getMenu2() {
        return menu2;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();

    }
}
