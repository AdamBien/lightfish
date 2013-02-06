/*
 Copyright 2012 Adam Bien, adam-bien.com

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.lightfish.business.monitoring.control;

import org.lightfish.business.monitoring.entity.Application;
import com.sun.jersey.api.client.Client;
import org.lightfish.business.monitoring.entity.ConnectionPool;
import org.lightfish.business.monitoring.entity.Snapshot;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import javax.enterprise.inject.Instance;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import java.util.logging.Logger;
import javax.enterprise.inject.Any;
import org.lightfish.business.monitoring.control.collectors.DataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
public class SnapshotProvider {
    
    public static final String HEAP_SIZE = "jvm/memory/usedheapsize-count";
    private static final String THREAD_COUNT = "jvm/thread-system/threadcount";
    private static final String PEAK_THREAD_COUNT = "jvm/thread-system/peakthreadcount";
    private static final String DEADLOCKED_THREADS = "jvm/thread-system/deadlockedthreads";
    private static final String ERROR_COUNT = "http-service/server/request/errorcount";
    private static final String AVG_PROCESSING_TIME = "http-service/server/request/processingtime";
    private static final String HTTP_BUSY_THREADS = "network/thread-pool/currentthreadsbusy";
    private static final String COMMITTED_TX = "transaction-service/committedcount";
    private static final String ROLLED_BACK_TX = "transaction-service/rolledbackcount";
    private static final String QUEUED_CONNS = "network/connection-queue/countqueued";
    private static final String CURRENT_SESSIONS = "web/session/activesessionscurrent";
    private static final String EXPIRED_SESSIONS = "web/session/expiredsessionstotal";
    private static final String APPLICATIONS = "applications";
    private static final Logger LOG = Logger.getLogger(SnapshotProvider.class.getName());
    static final String RESOURCES = "resources";
    private Client client;
    @Inject
    Instance<String> location;
    @Inject
    Instance<String> username;
    @Inject
    Instance<String> password;
    @Inject
    Instance<String> serverInstance;
    @Inject
    Instance<GlassfishAuthenticator> authenticator;
    @Inject
    @Any
    Instance<DataCollector> dataCollectors;
    
    @PostConstruct
    public void initializeClient() {
        this.client = Client.create();
    }
    
    public Snapshot fetchSnapshot() throws Exception {
        authenticator.get().addAuthenticator(client, username.get(), password.get());
        
        Snapshot snapshot = new Snapshot.Builder().build();
        for (DataCollector collector : dataCollectors) {
            DataPoint dataPoint = collector.collect();
            mapDataPointToSnapshot(dataPoint, snapshot);
        }
        
        return snapshot;
        
    }
    
    private void mapDataPointToSnapshot(DataPoint dataPoint, Snapshot snapshot) {
        switch (dataPoint.getName()) {
            case "applications":
                LOG.finest("Applications mapped to snapshot");
                snapshot.setApps((List<Application>) dataPoint.getValue());
                break;
            case "busyThreadCount":
                snapshot.setCurrentThreadBusy((Integer) dataPoint.getValue());
                break;
            case "commitedTransactions":
                snapshot.setCommittedTX((Integer) dataPoint.getValue());
                break;
            case "expiredSessionCount":
                snapshot.setExpiredSessions((Integer) dataPoint.getValue());
                break;
            case "sessionCount":
                snapshot.setActiveSessions((Integer) dataPoint.getValue());
                break;
            case "rolledBackTransactions":
                snapshot.setRolledBackTX((Integer) dataPoint.getValue());
                break;
            case "resources":
                snapshot.setPools((List<ConnectionPool>) dataPoint.getValue());
                break;
            case "queuedConnectionCount":
                snapshot.setQueuedConnections((Integer) dataPoint.getValue());
                break;
            case "errorCount":
                snapshot.setTotalErrors((Integer) dataPoint.getValue());
                break;
            case "deadLockedThreads":
                snapshot.setDeadlockedThreads((String) dataPoint.getValue());
                break;
            case "usedHeap":
                snapshot.setUsedHeapSize((Long) dataPoint.getValue());
                break;
            case "threadCount":
                snapshot.setThreadCount((Integer) dataPoint.getValue());
                break;
            case "peakThreadCount":
                snapshot.setPeakThreadCount((Integer) dataPoint.getValue());
                break;
            default:
                LOG.info("Data Point " + dataPoint.getName() + " could not be mapped to Snapshot");
                break;
        }
    }
}
