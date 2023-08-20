package org.laeq.editor.filter;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.DatabaseService;
import org.laeq.model.Video;
import org.laeq.video.filter.ALTMRetinexService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class DisplayController extends AbstractGriffonController {

    @MVCMember @Nonnull private DisplayModel model;
    @MVCMember @Nonnull private DisplayView view;

    @Inject private ALTMRetinexService altmRetinexService;

    @Inject
    private DatabaseService dbService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());

        File file = new File("src/main/resources/images/original.png");
        try {
            InputStream inputStream = new FileInputStream(file);
            Image image = new Image(inputStream);
            view.setImage(image);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void altmRetinex() {
        File file = new File("src/main/resources/images/original.png");

        altmRetinexService.controls(view.controls);
        BufferedImage bfImage = altmRetinexService.applyFilter(file);




        WritableImage image = SwingFXUtils.toFXImage(bfImage, null);

        view.setImage(image);
    }

    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }
}
