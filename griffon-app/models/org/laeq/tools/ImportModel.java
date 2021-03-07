package org.laeq.tools;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleStringProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Collection;
import org.laeq.model.Video;

import java.util.ArrayList;
import java.util.List;

@ArtifactProviderFor(GriffonModel.class)
public class ImportModel extends AbstractGriffonModel {
    public SimpleStringProperty filename = new SimpleStringProperty("");
    public SimpleStringProperty filePath = new SimpleStringProperty("");
    public SimpleStringProperty warning = new SimpleStringProperty("");

    public Video video;

    public List<Collection> collectionList = new ArrayList<>();

    public void setVideo(Video video) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Collection: id = %d - %s\n", video.getCollection().getId(), video.getCollection().toString()));
        builder.append(translate("z.categories\n"));
        video.getCollection().getCategories().forEach(c -> {
            builder.append(String.format("%d - %s\n", c.getId(), c.getName()));
        });
        builder.append("\n");

        warning.set(builder.toString());
    }

    private String translate(String key){
        return getApplication().getMessageSource().getMessage(key);
    }
}