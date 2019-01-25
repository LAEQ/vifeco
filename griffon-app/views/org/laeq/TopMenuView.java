package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

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
