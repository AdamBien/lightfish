package org.lightfish.business.monitoring.entity;

import javax.persistence.*;
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
@Entity
public class Snapshot {
    
    @Id
    @GeneratedValue
    private long id;
    @Temporal(TemporalType.TIME)
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

    @OneToMany(cascade= CascadeType.PERSIST)
    private List<ConnectionPool> pools;

    public Snapshot(long usedHeapSize, int threadCount, int peakThreadCount, int totalErrors, int currentThreadBusy, int committedTX, int rolledBackTX, int queuedConnections,int activeSessions) {
        this();
        this.usedHeapSize = usedHeapSize;
        this.threadCount = threadCount;
        this.peakThreadCount = peakThreadCount;
        this.totalErrors = totalErrors;
        this.currentThreadBusy = currentThreadBusy;
        this.committedTX = committedTX;
        this.rolledBackTX = rolledBackTX;
        this.queuedConnections = queuedConnections;
        this.activeSessions = activeSessions;
    }

    public Snapshot() {
        this.monitoringTime = new Date();
        this.pools = new ArrayList<ConnectionPool>();
    }

    public void add(ConnectionPool connectionPool) {
        this.pools.add(connectionPool);
    }

    public static class Builder{
        private Snapshot snapshot;

        public Builder() {
            this.snapshot = new Snapshot();
        }
        
        public Builder usedHeapSize(long usedHeapSize){
            snapshot.usedHeapSize = usedHeapSize;
            return this;
        }
        
        public Snapshot build(){
            return this.snapshot;
        }
    
    }
    
    public boolean isSuspicious(){
        return (currentThreadBusy > 100);
    }

    @Override
    public String toString() {
        return "Snapshot{" +
                "id=" + id +
                ", monitoringTime=" + monitoringTime +
                ", usedHeapSize=" + usedHeapSize +
                ", threadCount=" + threadCount +
                ", peakThreadCount=" + peakThreadCount +
                ", totalErrors=" + totalErrors +
                ", currentThreadBusy=" + currentThreadBusy +
                ", committedTX=" + committedTX +
                ", rolledBackTX=" + rolledBackTX +
                ", queuedConnections=" + queuedConnections +
                ", activeSessions=" + activeSessions +
                ", pools=" + pools +
                '}';
    }
}
