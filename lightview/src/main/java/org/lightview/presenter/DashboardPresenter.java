package org.lightview.presenter;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Worker;
import org.lightview.model.ConnectionPool;
import org.lightview.model.Snapshot;
import org.lightview.service.SnapshotProvider;

import java.util.List;

/**
 * User: blog.adam-bien.com
 * Date: 21.11.11
 * Time: 17:50
 */
public class DashboardPresenter implements DashboardPresenterBindings {

    private StringProperty uri;
    private ObservableList<Snapshot> snapshots;
    private ObservableMap<String, ConnectionPoolBindings> pools;
    SnapshotProvider service;
    private LongProperty usedHeapSizeInMB;
    private LongProperty threadCount;
    private IntegerProperty peakThreadCount;
    private IntegerProperty busyThreads;
    private IntegerProperty queuedConnections;
    private IntegerProperty commitCount;
    private IntegerProperty rollbackCount;
    private IntegerProperty totalErrors;
    private IntegerProperty activeSessions;
    private IntegerProperty expiredSessions;
    private LongProperty id;

    public DashboardPresenter() {
        this.snapshots = FXCollections.observableArrayList();
        this.pools = FXCollections.observableHashMap();
        this.uri = new SimpleStringProperty();
        this.usedHeapSizeInMB = new SimpleLongProperty();
        this.threadCount = new SimpleLongProperty();
        this.peakThreadCount = new SimpleIntegerProperty();
        this.busyThreads = new SimpleIntegerProperty();
        this.queuedConnections = new SimpleIntegerProperty();
        this.commitCount = new SimpleIntegerProperty();
        this.rollbackCount = new SimpleIntegerProperty();
        this.totalErrors = new SimpleIntegerProperty();
        this.activeSessions = new SimpleIntegerProperty();
        this.expiredSessions = new SimpleIntegerProperty();
        this.id = new SimpleLongProperty();
        this.initializeListeners();
    }

    void initializeListeners() {
        this.uri.addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observableValue, String s, String newUri) {
                restartService();
            }
        });
    }

    void restartService() {
        if (this.service != null && this.service.isRunning()) {
            this.service.cancel();
            this.service.reset();
        }
        this.startFetching();
    }


    public void setUri(String uri) {
        this.uri.setValue(uri);
    }

    public String getUri() {
        return this.uri.getValue();
    }

    public StringProperty getUriProperty() {
        return this.uri;
    }


    void startFetching() {
        this.service = new SnapshotProvider(getUri());
        service.start();
        service.valueProperty().addListener(
                new ChangeListener<Snapshot>() {

                    public void changed(ObservableValue<? extends Snapshot> observable, Snapshot old, Snapshot newValue) {
                        if (newValue != null) {
                            snapshots.add(newValue);
                            onSnapshotArrival(newValue);
                        }
                    }

                });
        registerRestarting();
    }

    void registerRestarting() {
        service.stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldState, Worker.State newState) {
                if (newState.equals(Worker.State.SUCCEEDED) || newState.equals(Worker.State.FAILED)) {
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
        this.activeSessions.set(snapshot.getActiveSessions());
        this.expiredSessions.set(snapshot.getExpiredSessions());
        this.id.set(snapshot.getId());
        this.updatePools(snapshot);
    }


    void updatePools(Snapshot snapshot) {
        List<ConnectionPool> connectionPools = snapshot.getPools();
        for (ConnectionPool connectionPool : connectionPools) {
            String jndiName = connectionPool.getJndiName();
            ConnectionPoolBindings bindings = ConnectionPoolBindings.from(connectionPool);
            ConnectionPoolBindings poolBindings = this.pools.get(jndiName);
            if(poolBindings != null){
                poolBindings.update(connectionPool);
            }else{
                this.pools.put(jndiName,bindings);
            }
        }
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

    public IntegerProperty getActiveSessions() {
        return activeSessions;
    }

    public IntegerProperty getExpiredSessions() {
        return expiredSessions;
    }

    public LongProperty getId() {
        return id;
    }

    public ObservableList<Snapshot> getSnapshots() {
        return snapshots;
    }

    public ObservableMap<String,ConnectionPoolBindings> getPools() {
        return pools;
    }
}
