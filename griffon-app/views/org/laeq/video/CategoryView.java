package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;

import java.util.Collections;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class CategoryView extends AbstractJavaFXGriffonView {
    private CategoryController controller;
    private CategoryModel model;

    @MVCMember @Nonnull
    private VifecoView parentView;

    @MVCMember
    public void setController(@Nonnull CategoryController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull CategoryModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();

        parentView.getMiddlePane().getItems().add(node);
    }
}
