package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.template.MiddlePaneView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class Player2View extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private Player2Controller controller;
    @MVCMember @Nonnull private Player2Model model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private AnchorPane controlPane;
    @FXML private AnchorPane playerPane;
    @FXML private AnchorPane categoryPane;


    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        init();
    }

    @Override
    public void mvcGroupDestroy(){
        destroyMVCGroup("controls");
        destroyMVCGroup("category");
        destroyMVCGroup("video_player");
    }

    private void init() {

    }

    public AnchorPane getControlPane() {
        return controlPane;
    }

    public AnchorPane getPlayerPane() {
        return playerPane;
    }

    public AnchorPane getCategoryPane() {
        return categoryPane;
    }
}
