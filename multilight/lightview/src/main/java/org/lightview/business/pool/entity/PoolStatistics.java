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

    private IntegerProperty currentThreadsWaiting = new SimpleIntegerProperty();

    public PoolStatistics(JsonObject statistics) {
        this.statistics = statistics;
    }

    public IntegerProperty currentThreadsWaiting() {
        int value = getThreadsStatistics().getInt("current");
        currentThreadsWaiting.set(value);
        return this.currentThreadsWaiting;

    }

    JsonObject getThreadsStatistics() {
        return statistics.getJsonObject(THREADS_STATISTICS);
    }

}
