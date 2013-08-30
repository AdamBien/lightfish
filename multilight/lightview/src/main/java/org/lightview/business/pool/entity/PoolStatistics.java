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

    public PoolStatistics(JsonObject statistics) {
        this.statistics = statistics;
    }

    public IntegerProperty currentThreadsWaiting() {
        int value = getThreadsStatistics().getInt("current");
        currentThreadsWaiting.set(value);
        return this.currentThreadsWaiting;
    }

    public IntegerProperty threadsWaitingHighwatermark() {
        int value = getThreadsStatistics().getInt("highwatermark");
        currentThreadsWaiting.set(value);
        return this.currentThreadsWaiting;
    }

    public IntegerProperty totalBeansCreated() {
        int value = statistics.getJsonObject("totalbeanscreated").getInt("count");
        totalBeansCreated.set(value);
        return totalBeansCreated;
    }

    public IntegerProperty totalBeansDestroyed() {
        int value = statistics.getJsonObject("totalbeansdestroyed").getInt("count");
        totalBeansDestroyed.set(value);
        return totalBeansDestroyed;
    }

    JsonObject getThreadsStatistics() {
        return statistics.getJsonObject(THREADS_STATISTICS);
    }

}
