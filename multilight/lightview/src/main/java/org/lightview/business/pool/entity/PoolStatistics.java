/*
 *
 */
package org.lightview.business.pool.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javax.json.JsonObject;

/**
 *
 * @author adam-bien.com
 */
public class PoolStatistics {

    private JsonObject statistics;

    private final String THREADS_STATISTICS = "numthreadswaiting";

    private final IntegerProperty currentThreadsWaiting = new SimpleIntegerProperty();
    private final IntegerProperty totalBeansCreated = new SimpleIntegerProperty();
    private final IntegerProperty totalBeansDestroyed = new SimpleIntegerProperty();
    private IntegerProperty NOT_AVAILABLE = new SimpleIntegerProperty(-1);

    public PoolStatistics(JsonObject statistics) {
        this.statistics = statistics;
    }

    public IntegerProperty currentThreadsWaitingProperty() {
        final JsonObject threadsStatistics = getThreadsStatistics();
        if (threadsStatistics == null) {
            return NOT_AVAILABLE;
        }
        int value = threadsStatistics.getInt("current");
        currentThreadsWaiting.set(value);
        return this.currentThreadsWaiting;
    }

    public int getCurrentThreadsWaiting() {
        return currentThreadsWaitingProperty().get();
    }

    public IntegerProperty threadsWaitingHighwatermarkProperty() {
        final JsonObject threadsStatistics = getThreadsStatistics();
        if (threadsStatistics == null) {
            return NOT_AVAILABLE;
        }
        int value = threadsStatistics.getInt("highwatermark");
        currentThreadsWaiting.set(value);
        return this.currentThreadsWaiting;
    }

    public int getThreadsWaitingHighwatermark() {
        return threadsWaitingHighwatermarkProperty().get();
    }

    public IntegerProperty totalBeansCreatedProperty() {
        if (statistics == null) {
            return NOT_AVAILABLE;
        }
        final JsonObject jsonObject = statistics.getJsonObject("totalbeanscreated");
        int value = jsonObject.getInt("count");
        totalBeansCreated.set(value);
        return totalBeansCreated;
    }

    public int getTotalBeansCreated() {
        return this.totalBeansCreatedProperty().get();
    }

    public IntegerProperty totalBeansDestroyedProperty() {
        if (statistics == null) {
            return NOT_AVAILABLE;
        }
        final JsonObject jsonObject = statistics.getJsonObject("totalbeansdestroyed");
        int value = jsonObject.getInt("count");
        totalBeansDestroyed.set(value);
        return totalBeansDestroyed;
    }

    public int getTotalBeansDestroyed() {
        return this.totalBeansCreatedProperty().get();
    }

    JsonObject getThreadsStatistics() {
        return statistics.getJsonObject(THREADS_STATISTICS);
    }

}
