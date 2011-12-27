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
    private int expiredSessions;
    
    @OneToMany(cascade= CascadeType.PERSIST)
    private List<ConnectionPool> pools;


    
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
        
        public Builder expiredSessions(int expiredSessions){
            snapshot.expiredSessions = expiredSessions;
            return this;
        }

        public Builder threadCount(int threadCount){
            snapshot.threadCount = threadCount;            
            return this;
        }

        public Builder peakThreadCount(int peakThreadCount){
            snapshot.peakThreadCount = peakThreadCount;
            return this;
        }

        public Builder totalErrors(int totalErrors){
            snapshot.totalErrors = totalErrors;
            return this;
        }
        public Builder currentThreadBusy(int currentThreadBusy){
            snapshot.currentThreadBusy = currentThreadBusy;
            return this;

        }
        public Builder committedTX(int committedTX){
            snapshot.committedTX = committedTX;
            return this;
        }
        public Builder rolledBackTX(int rolledBackTX){
            snapshot.rolledBackTX = rolledBackTX;
            return this;
        }
        public Builder queuedConnections(int queuedConnections){
            snapshot.queuedConnections = queuedConnections;
            return this;
        }
        public Builder activeSessions(int activeSessions){
            snapshot.activeSessions = activeSessions;
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
                ", expiredSessions=" + expiredSessions +
                ", pools=" + pools +
                '}';
    }
}
