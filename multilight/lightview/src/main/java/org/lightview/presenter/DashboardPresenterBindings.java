package org.lightview.presenter;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import org.lightview.model.Application;
import org.lightview.model.Snapshot;

/**
 * User: blog.adam-bien.com Date: 05.12.11 Time: 20:56
 */
public interface DashboardPresenterBindings {

    StringProperty getUriProperty();

    StringProperty getDeadlockedThreads();

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

    ReadOnlyIntegerProperty getExpiredSessions();

    ReadOnlyDoubleProperty getCommitsPerSecond();

    ReadOnlyDoubleProperty getRollbacksPerSecond();

    ObservableMap<String, ConnectionPoolBindings> getPools();

    ObservableList<Snapshot> getSnapshots();

    ObservableSet<Application> getApplications();

    EscalationsPresenterBindings getEscalationsPresenterBindings();

}
