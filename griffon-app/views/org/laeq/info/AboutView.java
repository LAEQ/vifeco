package org.laeq.info;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.template.MiddlePaneView;
import org.laeq.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Locale;

@ArtifactProviderFor(GriffonView.class)
public class AboutView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private AboutController controller;
    @MVCMember @Nonnull private MiddlePaneView parentView;
    @Inject private PreferencesService preferenceService;

    @FXML private WebView citationView;
    @FXML private WebView aboutView;
    @FXML private WebView helpView;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);

        WebEngine webEngine = citationView.getEngine();
        String aboutPath = String.format("html/about_%s.html", preferenceService.getPreferences().locale.getLanguage());
        webEngine.load(getClass().getClassLoader().getResource(aboutPath).toExternalForm());

        webEngine = helpView.getEngine();
        String helpPath = String.format("html/help_%s.html", preferenceService.getPreferences().locale.getLanguage());
        webEngine.load(getClass().getClassLoader().getResource(helpPath).toExternalForm());

        webEngine = aboutView.getEngine();
        String citationView = String.format("html/citation_%s.html", preferenceService.getPreferences().locale.getLanguage());
        webEngine.load(getClass().getClassLoader().getResource(citationView).toExternalForm());

    }


    public void changeLocale(Locale locale) {
        WebEngine webEngine = citationView.getEngine();
        String aboutPath = String.format("html/about_%s.html", locale.getLanguage());
        webEngine.load(getClass().getClassLoader().getResource(aboutPath).toExternalForm());

        webEngine = helpView.getEngine();
        String helpPath = String.format("html/help_%s.html", locale.getLanguage());
        webEngine.load(getClass().getClassLoader().getResource(helpPath).toExternalForm());

        webEngine = aboutView.getEngine();
        String citationView = String.format("html/citation_%s.html", locale.getLanguage());
        webEngine.load(getClass().getClassLoader().getResource(citationView).toExternalForm());

    }
}
