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
package org.lightfish.business.servermonitoring.boundary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJBException;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.lightfish.business.servermonitoring.control.BeatListener;
import org.lightfish.business.servermonitoring.control.SessionTokenRetriever;
import org.lightfish.business.servermonitoring.control.collectors.DataPoint;
import org.lightfish.business.servermonitoring.control.collectors.ParallelDataCollectionAction;
import org.lightfish.business.servermonitoring.control.collectors.ParallelDataCollectionExecutor;
import org.lightfish.business.servermonitoring.entity.Application;
import org.lightfish.business.servermonitoring.entity.ConnectionPool;
import org.lightfish.business.servermonitoring.entity.LogRecord;
import org.lightfish.business.servermonitoring.entity.Snapshot;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Singleton
@Path("snapshots")
@Produces(MediaType.APPLICATION_JSON)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class MonitoringController {

    public static final String COMBINED_SNAPSHOT_NAME = "__all__";
    @Inject
    private Logger LOG;
    @Inject
    Instance<SnapshotCollector> snapshotCollectorInstance;
    @Inject
    ParallelDataCollectionExecutor parallelCollector;
    @Inject
    ParallelDataCollectionAction parallelAction;
    @PersistenceContext
    EntityManager em;
    @Inject
    @Severity(Severity.Level.HEARTBEAT)
    Event<Snapshot> heartBeat;
    @Inject
    Instance<BeatListener> beatListeners;
    @Inject
    Instance<String[]> serverInstances;
    @Inject
    Instance<Integer> collectionTimeout;
    @Inject
    SessionTokenRetriever sessionTokenProvider;
    int nextInstanceIndex = 0;
    ConcurrentMap<String, SnapshotCollectionAction> currentCollectionActions = new ConcurrentHashMap<>();
    ConcurrentMap<String, Snapshot> currentSnapshots = new ConcurrentHashMap<>();
    @Resource
    TimerService timerService;
    private Timer timer;
    @Inject
    private Instance<Integer> interval;

    public void startTimer() {
        sessionTokenProvider.retrieveSessionToken();
        ScheduleExpression expression = new ScheduleExpression();
        expression.minute("*").second("*/" + interval.get()).hour("*");
        this.timer = this.timerService.createCalendarTimer(expression);
    }

    public void restart() {
        stopTimer();
        startTimer();
    }

    public Date nextTimeout() {
        if (timer != null) {
            return timer.getNextTimeout();
        }
        return null;
    }

    @Timeout
    public void gatherAndPersist() {
        handleCompletedFutures(false);
        handleTimedOutFutures();
        notifyBeatListeners();

        startNextInstanceCollection(nextInstanceIndex++);

        if (nextInstanceIndex >= serverInstances.get().length) {
            if (!currentCollectionActions.isEmpty()) {
                LOG.info("Waiting for completed futures");
                handleCompletedFutures(true);
            } else {
                LOG.info("Nothing todo, no data to collect");
            }
            handleRoundCompletion();
            nextInstanceIndex = 0;
        }

        LOG.info(".");
    }

    private void handleCompletedFutures(Boolean wait) {
        List<Snapshot> handledSnapshots = new ArrayList<>();

        Iterator<Entry<String, SnapshotCollectionAction>> it = currentCollectionActions.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, SnapshotCollectionAction> entry = it.next();
            try {
                DataPoint<Snapshot> dataPoint;
                if (wait) {
                    dataPoint = entry.getValue().getFuture().get();
                } else {
                    dataPoint = entry.getValue().getFuture().get(0, TimeUnit.NANOSECONDS);
                }

                LOG.log(Level.FINE, "Handling completed snapshot for {0}", entry.getKey());
                it.remove();
                if (dataPoint == null) {
                    LOG.log(Level.WARNING, "An exception occured while retrieving data for " + entry);
                    continue;
                }

                LOG.fine("Adding completed snapshot to currentSnapshots list");
                currentSnapshots.put(entry.getKey(), dataPoint.getValue());

                LOG.log(Level.FINER, "Persisting {0}", dataPoint.getValue());

                handledSnapshots.add(dataPoint.getValue());

                LOG.log(Level.FINE, "Done handling completed snapshot for {0}", entry.getKey());
            } catch (InterruptedException ex) {
                LOG.log(Level.FINE, "The snapshot collection for " + entry.getKey() + " was interrupted", ex);
            } catch (ExecutionException ex) {
                LOG.log(Level.SEVERE, "The snapshot collection for " + entry.getKey() + " resulted in an exception", ex);
                entry.getValue().getFuture().cancel(false);
                it.remove();
            } catch (TimeoutException ex) {
                LOG.log(Level.FINEST, "The snapshot collection for " + entry.getKey() + " has not completed", ex);
            }

            //  em.flush();
            for (Snapshot snapshot : handledSnapshots) {
                fireHeartbeat(snapshot);
            }

        }
    }

    private void handleTimedOutFutures() {

        Calendar timedOutCal = Calendar.getInstance();
        timedOutCal.add(Calendar.SECOND, -(collectionTimeout.get()));

        Long timedOutTime = timedOutCal.getTimeInMillis();

        Iterator<Entry<String, SnapshotCollectionAction>> it = currentCollectionActions.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, SnapshotCollectionAction> entry = it.next();
            if (timedOutTime > entry.getValue().getStarted()) {
                LOG.log(Level.WARNING, "The snapshot collection for {0} timed out.", entry.getKey());
                it.remove();
            }
        }
    }

    private void handleRoundCompletion() {
        LOG.info("All snapshots collected for this round!");
        Snapshot combinedSnapshot = combineSnapshots(currentSnapshots.values());
        em.persist(combinedSnapshot);
        fireHeartbeat(combinedSnapshot);

        currentSnapshots.clear();
        currentCollectionActions.clear();
    }

    private void startNextInstanceCollection(int index) {
        String currentServerInstance = null;
        try {
            currentServerInstance = serverInstances.get()[index];
            LOG.info("Monitoring instance: " + currentServerInstance);
        } catch (ArrayIndexOutOfBoundsException oobEx) {
            LOG.warning("It appears you changed the server instances you are monitoring while the timer is running, this is not recommended...");
            return;
        }

        SnapshotCollector collector = snapshotCollectorInstance.get();
        collector.setServerInstance(currentServerInstance);

        LOG.log(Level.INFO, "Starting data collection for {0}", currentServerInstance);
        Future<DataPoint<Snapshot>> future = parallelAction.compute(collector);
        currentCollectionActions.put(currentServerInstance,
                new SnapshotCollectionAction(future));
    }

    private Snapshot combineSnapshots(Collection<Snapshot> snapshots) {

        long usedHeapSize = 0l;
        int threadCount = 0;
        int peakThreadCount = 0;
        int totalErrors = 0;
        int currentThreadBusy = 0;
        int committedTX = 0;
        int rolledBackTX = 0;
        int queuedConnections = 0;
        int activeSessions = 0;
        int expiredSessions = 0;
        //List<Application> applications = new ArrayList<>();
        Map<String, Application> applications = new HashMap<>();
        Map<String, ConnectionPool> pools = new HashMap<>();
        Set<LogRecord> logs = new TreeSet<>();
        for (Snapshot current : snapshots) {
            usedHeapSize += current.getUsedHeapSize();
            threadCount += current.getThreadCount();
            peakThreadCount += current.getPeakThreadCount();
            totalErrors += current.getTotalErrors();
            currentThreadBusy += current.getCurrentThreadBusy();
            committedTX += current.getCommittedTX();
            rolledBackTX += current.getRolledBackTX();
            queuedConnections += current.getQueuedConnections();
            activeSessions += current.getActiveSessions();
            expiredSessions += current.getExpiredSessions();
            for (Application application : current.getApps()) {
                Application combinedApp = new Application(application.getApplicationName(), application.getComponents());
                applications.put(application.getApplicationName(), combinedApp);
            }

            for (ConnectionPool currentPool : current.getPools()) {
                ConnectionPool combinedPool = pools.get(currentPool.getJndiName());
                if (combinedPool == null) {
                    combinedPool = new ConnectionPool();
                    combinedPool.setJndiName(currentPool.getJndiName());
                    pools.put(currentPool.getJndiName(), combinedPool);
                }
                combinedPool.setNumconnfree(currentPool.getNumconnfree() + combinedPool.getNumconnfree());
                combinedPool.setNumconnused(currentPool.getNumconnused() + combinedPool.getNumconnused());
                combinedPool.setNumpotentialconnleak(currentPool.getNumpotentialconnleak() + combinedPool.getNumpotentialconnleak());
                combinedPool.setWaitqueuelength(currentPool.getWaitqueuelength() + combinedPool.getWaitqueuelength());
            }

            //logs.addAll(current.getLogRecords());
        }

        Snapshot combined = new Snapshot.Builder()
                .activeSessions(activeSessions)
                .committedTX(committedTX)
                .currentThreadBusy(currentThreadBusy)
                .expiredSessions(expiredSessions)
                .peakThreadCount(peakThreadCount)
                .queuedConnections(queuedConnections)
                .rolledBackTX(rolledBackTX)
                .threadCount(threadCount)
                .totalErrors(totalErrors)
                .usedHeapSize(usedHeapSize)
                .instanceName(COMBINED_SNAPSHOT_NAME)
                .logs(new ArrayList<>(logs))
                .build();
        combined.setApps(new ArrayList(applications.values()));
        combined.setPools(new ArrayList(pools.values()));

        return combined;
    }

    @GET
    public List<Snapshot> all() {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery();
        CriteriaQuery<Snapshot> select = q.select(q.from(Snapshot.class));
        return this.em.createQuery(select)
                .getResultList();

    }

    @PreDestroy
    public void stopTimer() {
        if (timer != null) {
            try {
                this.timer.cancel();
            } catch (IllegalStateException | EJBException e) {
                LOG.log(Level.SEVERE, "Cannot cancel timer " + this.timer, e);
            } finally {
                this.timer = null;
            }
        }
    }

    private void fireHeartbeat(Snapshot snapshot) {
        LOG.log(Level.FINE, "Firing heartbeat for {0}", snapshot.getInstanceName());
        try {
            heartBeat.fire(snapshot);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cannot fire heartbeat for " + snapshot.getInstanceName(), e);
        }
    }

    public boolean isRunning() {
        return (this.timer != null);
    }

    void notifyBeatListeners() {
        for (BeatListener listener : this.beatListeners) {
            listener.onBeat();
        }
    }

    private class SnapshotCollectionAction {

        private final Long started;
        private Future<DataPoint<Snapshot>> future;

        public SnapshotCollectionAction(Future<DataPoint<Snapshot>> future) {
            this.started = Calendar.getInstance().getTimeInMillis();
            LOG.info("Got future: " + future);
            this.future = future;
        }

        public Long getStarted() {
            return started;
        }

        public Future<DataPoint<Snapshot>> getFuture() {
            return future;
        }
    }
}
