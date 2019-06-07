package org.laeq.system;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.TranslationService;
import org.laeq.template.MiddlePaneView;
import org.laeq.user.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;

@ArtifactProviderFor(GriffonView.class)
public class StatusView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private StatusController controller;
    @MVCMember @Nonnull private StatusModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;
    @Inject
    private PreferencesService preferencesService;

    @FXML private Text connectionStatus;
    @FXML private Text tableStatus;
    @FXML private Text userStatus;
    @FXML private Text collectionStatus;
    @FXML private Label totalVideo;
    @FXML private Button exportActionTarget;
    @FXML private Button archiveActionTarget;
    @FXML private Text title;
    @FXML private Text connectionTxt;
    @FXML private Text tableTxt;
    @FXML private Text userTxt;
    @FXML private Text collectionTxt;
    @FXML private Label videoLabel;
    @FXML private Text tableTitleTxt;


    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);

        connectActions(node, controller);

        connectionStatus.textProperty().bind(model.connectionStatusProperty());
        tableStatus.textProperty().bind(model.tableStatusProperty());
        userStatus.textProperty().bind(model.userStatusProperty());
        collectionStatus.textProperty().bind(model.collectionStatusProperty());
        totalVideo.textProperty().bind(model.videoTotalProperty().asString());


        translateText();
    }

    @Override
    public void mvcGroupDestroy(){
        //@todo
    }

    public void translateText(){
        try {
            TranslationService translationService = new TranslationService(getClass().getClassLoader().getResourceAsStream("messages/messages.json"), preferencesService.getPreferences().locale);

            exportActionTarget.setText(translationService.getMessage("org.laeq.status.btn.export"));
            archiveActionTarget.setText(translationService.getMessage("org.laeq.status.btn.archive"));
            title.setText(translationService.getMessage("org.laeq.status.title"));
            connectionTxt.setText(translationService.getMessage("org.laeq.status.connection"));
            tableTxt.setText(translationService.getMessage("org.laeq.status.table"));
            collectionTxt.setText(translationService.getMessage("org.laeq.status.collection"));
            userTxt.setText(translationService.getMessage("org.laeq.status.default_user"));
            videoLabel.setText(translationService.getMessage("org.laeq.video.label"));
            tableTitleTxt.setText(translationService.getMessage("org.laeq.table.title"));


        } catch (IOException e) {
            getLog().error("Cannot load file messages.json");
        }
    }
}
