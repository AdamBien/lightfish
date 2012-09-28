package org.lightview.model;

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
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
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
    private int activeSessions;
    private int expiredSessions;
    private String deadlockedThreads;
    private List<ConnectionPool> pools;
    private List<Application> apps;

    public Snapshot() {
        this.monitoringTime = new Date();
        this.pools = new ArrayList<>();
        this.apps = new ArrayList<>();
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

    public long getUsedHeapSize() {
        return usedHeapSize;
    }

    public int getExpiredSessions() {
        return expiredSessions;
    }

    public int getActiveSessions() {
        return activeSessions;
    }

    public String getDeadlockedThreads() {
        return deadlockedThreads;
    }

    public List<ConnectionPool> getPools() {
        return pools;
    }

    public List<Application> getApps() {
        return apps;
    }

    public ConnectionPool getPool(String jndiName) {
        for (ConnectionPool pool : pools) {
            if (jndiName.equals(pool.getJndiName())) {
                return pool;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Snapshot{" + "id=" + id + ", monitoringTime=" + monitoringTime + ", usedHeapSize=" + usedHeapSize + ", threadCount=" + threadCount + ", peakThreadCount=" + peakThreadCount + ", totalErrors=" + totalErrors + ", currentThreadBusy=" + currentThreadBusy + ", committedTX=" + committedTX + ", rolledBackTX=" + rolledBackTX + ", queuedConnections=" + queuedConnections + ", activeSessions=" + activeSessions + ", expiredSessions=" + expiredSessions + ", pools=" + pools + ", apps=" + apps + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Snapshot other = (Snapshot) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
