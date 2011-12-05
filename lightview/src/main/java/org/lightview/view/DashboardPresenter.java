package org.lightview.view;

import javafx.beans.property.*;
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
public class DashboardPresenter implements DashboardPresenterBindings {

    private StringProperty uri;
    private ObservableList<Snapshot> snapshots;
    SnapshotProvider service;
    private LongProperty usedHeapSizeInMB;
    private LongProperty threadCount;
    private IntegerProperty peakThreadCount;
    private IntegerProperty busyThreads;
    private IntegerProperty queuedConnections;
    private IntegerProperty commitCount;
    private IntegerProperty rollbackCount;
    private IntegerProperty totalErrors;

    public DashboardPresenter(){
        this.snapshots = FXCollections.observableArrayList();
        this.uri = new SimpleStringProperty();
        this.usedHeapSizeInMB = new SimpleLongProperty();
        this.threadCount = new SimpleLongProperty();
        this.peakThreadCount = new SimpleIntegerProperty();
        this.busyThreads = new SimpleIntegerProperty();
        this.queuedConnections = new SimpleIntegerProperty();
        this.commitCount = new SimpleIntegerProperty();
        this.rollbackCount = new SimpleIntegerProperty();
        this.totalErrors = new SimpleIntegerProperty();
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


     void startFetching() {
        this.service = new SnapshotProvider(getUri());
        service.start();
        service.valueProperty().addListener(
                new ChangeListener<Snapshot>() {

                    public void changed(ObservableValue<? extends Snapshot> observable, Snapshot old, Snapshot newValue) {
                        if(newValue != null){
                            snapshots.add(newValue);
                                onSnapshotArrival(newValue);
                        }
                    }

                });
         registerRestarting();
     }

    void registerRestarting() {
        service.stateProperty().addListener(new ChangeListener<Worker.State>(){
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldState, Worker.State newState) {
                if(newState.equals(Worker.State.SUCCEEDED) || newState.equals(Worker.State.FAILED)){
                    service.reset();
                    service.start();
                }
            }

        });
    }

    void onSnapshotArrival(Snapshot snapshot) {
        this.usedHeapSizeInMB.set(snapshot.getUsedHeapSizeInMB());
        this.threadCount.set(snapshot.getThreadCount());
        this.peakThreadCount.set(snapshot.getPeakThreadCount());
        this.busyThreads.set(snapshot.getCurrentThreadBusy());
        this.queuedConnections.set(snapshot.getQueuedConnections());
        this.commitCount.set(snapshot.getCommittedTX());
        this.rollbackCount.set(snapshot.getRolledBackTX());
        this.totalErrors.set(snapshot.getTotalErrors());

    }

    public LongProperty getUsedHeapSizeInMB() {
        return usedHeapSizeInMB;
    }

    public LongProperty getThreadCount() {
        return threadCount;
    }

    public IntegerProperty getPeakThreadCount() {
        return peakThreadCount;
    }

    public IntegerProperty getBusyThreads() {
        return busyThreads;
    }

    public IntegerProperty getQueuedConnections() {
        return queuedConnections;
    }

    public IntegerProperty getCommitCount() {
        return commitCount;
    }

    public IntegerProperty getRollbackCount() {
        return rollbackCount;
    }

    public IntegerProperty getTotalErrors() {
        return totalErrors;
    }

    public ObservableList<Snapshot> getSnapshots() {
        return snapshots;
    }
}
