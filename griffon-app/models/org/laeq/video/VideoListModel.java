package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonModel.class)
public class VideoListModel extends AbstractGriffonModel {
    private StringProperty clickCount;

    @Nonnull
    public final StringProperty clickCountProperty() {
        if (clickCount == null) {
            clickCount = new SimpleStringProperty(this, "clickCount", "0");
        }
        return clickCount;
    }

    public void setClickCount(String clickCount) {
        clickCountProperty().set(clickCount);
    }

    public String getClickCount() {
        return clickCountProperty().get();
    }
}