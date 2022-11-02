package org.laeq.editor;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPointColorized;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

@ArtifactProviderFor(GriffonModel.class)
public final class EditorModel extends AbstractGriffonModel {
    @MVCMember @Nonnull private Video video;

    //Video controls
    public final Controls controls = new Controls();
    public final ImageControls imageControls = new ImageControls();
    public final SimpleBooleanProperty isPlaying = new SimpleBooleanProperty(false);

    //Property for normalizing the icon position
    public final SimpleDoubleProperty width = new SimpleDoubleProperty(1);
    public final SimpleDoubleProperty height = new SimpleDoubleProperty(1);

    private final Map<String, Category> shortcutMap= new HashMap();
    public final SimpleBooleanProperty isReady = new SimpleBooleanProperty(Boolean.FALSE);


    public void setVideo(@Nonnull Video video){
        this.video = video;
    }

    public Video getVideo() {
        return video;
    }
}