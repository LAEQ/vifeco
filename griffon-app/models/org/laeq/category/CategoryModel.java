package org.laeq.category;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleStringProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonModel.class)
public class CategoryModel extends AbstractGriffonModel {
    private SimpleStringProperty name;
    private SimpleStringProperty icon;
    private SimpleStringProperty shortcut;

    public String getName() {
        return name.get();
    }
    @Nonnull
    public SimpleStringProperty nameProperty() {
        if (name == null) {
            name = new SimpleStringProperty(this, "name", "0");
        }

        return name;
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public String getIcon() {
        return icon.get();
    }
    @Nonnull
    public SimpleStringProperty iconProperty() {
        if (icon == null) {
            icon = new SimpleStringProperty(this, "icon", "0");
        }

        return icon;
    }
    public void setIcon(String icon) {
        this.icon.set(icon);
    }

    public String getShortcut() {
        return shortcut.get();
    }
    @Nonnull
    public SimpleStringProperty shortcutProperty() {
        if (shortcut == null) {
            shortcut = new SimpleStringProperty(this, "shortcut", "0");
        }

        return shortcut;
    }
    public void setShortcut(String shortcut) {
        this.shortcut.set(shortcut);
    }
}