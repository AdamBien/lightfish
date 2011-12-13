package org.lightview.presenter;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.lightview.model.Snapshot;

/**
 * User: blog.adam-bien.com
 * Date: 05.12.11
 * Time: 20:56
 */
public interface DashboardPresenterBindings {
    StringProperty getUriProperty();
    ReadOnlyLongProperty getId();
    ReadOnlyLongProperty getUsedHeapSizeInMB();
    ReadOnlyLongProperty getThreadCount();
    ReadOnlyIntegerProperty getPeakThreadCount();
    ReadOnlyIntegerProperty getBusyThreads();
    ReadOnlyIntegerProperty getQueuedConnections();
    ReadOnlyIntegerProperty getCommitCount();
    ReadOnlyIntegerProperty getRollbackCount();
    ReadOnlyIntegerProperty getTotalErrors();
    ReadOnlyIntegerProperty getActiveSessions();
    ObservableMap<String, ConnectionPoolBindings> getPools();
    ObservableList<Snapshot> getSnapshots();
}
