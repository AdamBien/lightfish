/*
 *
 */
package org.lightview.presentation.dashboard;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.annotation.PostConstruct;
import org.lightview.model.Snapshot;

/**
 *
 * @author adam-bien.com
 */
public class DashboardModel {

    private ObjectProperty<Snapshot> current;

    private StringProperty serverUri;

    @PostConstruct
    public void init() {
        this.current = new SimpleObjectProperty<>();
        this.serverUri = new SimpleStringProperty();
    }

    public ObjectProperty<Snapshot> currentSnapshotProperty() {
        return current;
    }

    public StringProperty serverUriProperty() {
        return this.serverUri;
    }

    public void setUri(String uri) {
        this.serverUri.setValue(uri);
    }

    public String getUri() {
        return this.serverUri.getValue();
    }

}
