package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.Node;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class BottomView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private BottomController controller;
    @MVCMember @Nonnull private BottomModel model;

    @MVCMember @Nonnull private VifecoView parentView;

    @Override
    public void initUI() {
        Node node = loadFromFXML();

        connectActions(node, controller);

        parentView.getBottom().getChildren().add(node);
    }
}
