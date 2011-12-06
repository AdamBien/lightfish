package org.lightview.presenter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.lightview.entity.Snapshot;

/**
 * User: blog.adam-bien.com
 * Date: 05.12.11
 * Time: 20:56
 */
public interface DashboardPresenterBindings {
    LongProperty getUsedHeapSizeInMB();

    LongProperty getThreadCount();

    IntegerProperty getPeakThreadCount();

    IntegerProperty getBusyThreads();

    IntegerProperty getQueuedConnections();

    IntegerProperty getCommitCount();

    IntegerProperty getRollbackCount();

    IntegerProperty getTotalErrors();

    ObservableList<Snapshot> getSnapshots();

    StringProperty getUriProperty();

    ObservableMap<String, ConnectionPoolBindings> getPools();

    LongProperty getId();
}
