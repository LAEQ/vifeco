package org.laeq.info;

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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.template.MiddlePaneView;

import java.util.Collections;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class AboutView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private AboutController controller;
    @MVCMember @Nonnull private AboutModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private WebView citationView;
//    @FXML private WebView aboutView;
//    @FXML private WebView helpView;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);

        citationView.getEngine().load("http://google.com");

//        WebEngine webEngine = citationView.getEngine();
//        String content = "Hello World!";
////        webEngine.loadContent(content, "text/html");

        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
//        connectActions(node, controller);
    }

}
