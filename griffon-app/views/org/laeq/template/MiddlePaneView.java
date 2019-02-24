package org.laeq.template;

import griffon.core.artifact.GriffonView;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class MiddlePaneView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private MiddlePaneController controller;
    @MVCMember @Nonnull private MiddlePaneModel model;
    @MVCMember @Nonnull private VifecoView parentView;

    @FXML private AnchorPane middlePane;

    private String mvcGroupId;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        mvcGroupId = "";

        parentView.getMiddlePane().getItems().add(node);
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void addMVCGroup(String mvcGroupId, Node node){
        this.setMvcGroupId(mvcGroupId);
        this.middlePane.getChildren().add(node);

    }

    private void setMvcGroupId(String mvcGroupId) {
        if(this.mvcGroupId.length() > 0){
            destroyMVCGroup(this.mvcGroupId);
        }

        this.mvcGroupId = mvcGroupId;
    }

    @Nonnull
    public MiddlePaneModel getModel() {
        return model;
    }
}
