package org.lightview.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import org.lightview.entity.Snapshot;
import org.lightview.service.SnapshotProvider;

/**
 * User: blog.adam-bien.com
 * Date: 21.11.11
 * Time: 17:50
 */
public class DashboardPresenter {

    private SnapshotListener snapshotListener;
    private StringProperty uri = new SimpleStringProperty();
    private ObservableList<Snapshot> snapshots = FXCollections.observableArrayList();
    SnapshotProvider service;

    public DashboardPresenter(){
        this.initializeListeners();
    }

    void initializeListeners(){
        this.uri.addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observableValue, String s, String newUri) {
                restartService();
            }
        });
    }

    void restartService() {
        if(this.service != null && this.service.isRunning()){
            this.service.cancel();
            this.service.reset();
        }
        this.startFetching();
    }


    public void setUri(String uri){
        this.uri.setValue(uri);
    }

    public String getUri(){
        return this.uri.getValue();
    }

    public StringProperty getUriProperty(){
        return this.uri;
    }

    public void addSnapshotsObserver(SnapshotListener listener){
        this.snapshotListener = listener;
    }


     void startFetching() {
        this.service = new SnapshotProvider(getUri());
        service.start();
        service.valueProperty().addListener(
                new ChangeListener<Snapshot>() {

                    public void changed(ObservableValue<? extends Snapshot> arg0, Snapshot old, Snapshot newValue) {
                        if(newValue != null){
                            snapshots.add(newValue);
                            if(snapshotListener != null) {
                                snapshotListener.onSnapshotArrival(newValue);
                            }
                        }
                    }
                });
        service.stateProperty().addListener(new ChangeListener<Worker.State>(){
            public void changed(ObservableValue<? extends Worker.State> arg0, Worker.State arg1, Worker.State arg2) {
                if(arg2.equals(Worker.State.SUCCEEDED)){
                    service.reset();
                    service.start();
                }
            }

        });
}

    public ObservableList<Snapshot> getSnapshots() {
        return snapshots;
    }
}
