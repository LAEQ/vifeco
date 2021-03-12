package org.laeq;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleStringProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;


@ArtifactProviderFor(GriffonModel.class)
public class ConfigModel extends AbstractGriffonModel {

    public SimpleStringProperty title = new SimpleStringProperty();
    public SimpleStringProperty version = new SimpleStringProperty();
    public SimpleStringProperty buildDate = new SimpleStringProperty();


    public SimpleStringProperty rootDir = new SimpleStringProperty();
    public SimpleStringProperty exportDir = new SimpleStringProperty();

    public SimpleStringProperty javaVersion = new SimpleStringProperty();
    public SimpleStringProperty fxVersion = new SimpleStringProperty();

    public SimpleStringProperty latestVersion = new SimpleStringProperty();
}