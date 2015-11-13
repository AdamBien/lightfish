/*
 *
 */
package org.lightview.presentation.dashboard;

import java.util.HashSet;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javax.annotation.PostConstruct;
import org.lightview.model.Application;
import org.lightview.model.Snapshot;

/**
 *
 * @author adam-bien.com
 */
public class DashboardModel {

    private ObjectProperty<Snapshot> current;

    private StringProperty serverUri;
    private ObservableSet<Application> applications;

    @PostConstruct
    public void init() {
        this.current = new SimpleObjectProperty<>();
        this.serverUri = new SimpleStringProperty();
        this.serverUri.setValue("http://localhost:8080/lightfish");
        this.applications = FXCollections.observableSet(new HashSet<Application>());

    }

    public ObservableSet<Application> applicationsSetProperty() {
        return this.applications;
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

    public void updateApplications(List<Application> apps) {
        this.applications.addAll(apps);
    }
}
