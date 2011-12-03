package org.lightview.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Snapshot {
    
    private long id;
    private Date monitoringTime;
    private long usedHeapSize;
    private int threadCount;
    private int peakThreadCount;
    private int totalErrors;
    private int currentThreadBusy;
    private int committedTX;
    private int rolledBackTX;
    private int queuedConnections;

    private List<ConnectionPool> pools;

    public Snapshot(long usedHeapSize, int threadCount, int peakThreadCount, int totalErrors, int currentThreadBusy, int committedTX, int rolledBackTX, int queuedConnections) {
        this();
        this.usedHeapSize = usedHeapSize;
        this.threadCount = threadCount;
        this.peakThreadCount = peakThreadCount;
        this.totalErrors = totalErrors;
        this.currentThreadBusy = currentThreadBusy;
        this.committedTX = committedTX;
        this.rolledBackTX = rolledBackTX;
        this.queuedConnections = queuedConnections;
    }

    public Snapshot() {
        this.monitoringTime = new Date();
        this.pools = new ArrayList<ConnectionPool>();
    }

    public long getId() {
        return id;
    }

    public Date getMonitoringTime() {
        return monitoringTime;
    }

    public long getUsedHeapSizeInMB() {
        return (usedHeapSize / 1024) / 1024;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public int getPeakThreadCount() {
        return peakThreadCount;
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public int getCurrentThreadBusy() {
        return currentThreadBusy;
    }

    public int getCommittedTX() {
        return committedTX;
    }

    public int getRolledBackTX() {
        return rolledBackTX;
    }

    public int getQueuedConnections() {
        return queuedConnections;
    }

    @Override
    public String toString() {
        return "Snapshot{" + "id=" + id + ", monitoringTime=" + monitoringTime + ", usedHeapSize=" + usedHeapSize + ", threadCount=" + threadCount + ", totalErrors=" + totalErrors + ", currentThreadBusy=" + currentThreadBusy + ", committedTX=" + committedTX + ", rolledBackTX=" + rolledBackTX + ", queuedConnections=" + queuedConnections + '}';
    }
}
