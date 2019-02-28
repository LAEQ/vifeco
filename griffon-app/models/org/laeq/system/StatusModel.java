package org.laeq.system;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleStringProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

@ArtifactProviderFor(GriffonModel.class)
public class StatusModel extends AbstractGriffonModel {
    private SimpleStringProperty connectionStatus;
    private SimpleStringProperty tableStatus;
    private SimpleStringProperty userStatus;
    private SimpleStringProperty collectionStatus;

    public StatusModel() {
        connectionStatus = new SimpleStringProperty(this, "connectionStatus", "");
        tableStatus = new SimpleStringProperty(this, "tableStatus", "");
        userStatus = new SimpleStringProperty(this, "userStatus", "");
        collectionStatus = new SimpleStringProperty(this, "collectionStatus", "");
    }

    public String getConnectionStatus() {
        return connectionStatus.get();
    }
    public SimpleStringProperty connectionStatusProperty() {
        return connectionStatus;
    }
    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus.set(connectionStatus);
    }

    public String getTableStatus() {
        return tableStatus.get();
    }
    public SimpleStringProperty tableStatusProperty() {
        return tableStatus;
    }
    public void setTableStatus(String tableStatus) {
        this.tableStatus.set(tableStatus);
    }

    public String getUserStatus() {
        return userStatus.get();
    }
    public SimpleStringProperty userStatusProperty() {
        return userStatus;
    }
    public void setUserStatus(String userStatus) {
        this.userStatus.set(userStatus);
    }

    public String getCollectionStatus() {
        return collectionStatus.get();
    }
    public SimpleStringProperty collectionStatusProperty() {
        return collectionStatus;
    }
    public void setCollectionStatus(String collectionStatus) {
        this.collectionStatus.set(collectionStatus);
    }
}