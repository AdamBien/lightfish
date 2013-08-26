package org.lightfish.business.monitoring.control;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class SnapshotProviderIT {

    SnapshotProvider dataProvider;

    @Before
    public void initialize() {
        this.dataProvider = new SnapshotProvider();
//        this.dataProvider.location = mock(Instance.class);
//        when(this.dataProvider.location.get()).thenReturn("localhost:4848");
//        this.dataProvider.initializeClient();
    }

    @Test
    public void fetchHeap() {
//        long usedHeapSize = this.dataProvider.usedHeapSize();
//        System.out.println("Used heap size: " + usedHeapSize);
//        assertTrue(usedHeapSize > 0);
    }

//    @Test
//    public void threadCount() throws JSONException{
//        int threadCount = this.dataProvider.threadCount();
//        System.out.println("Thread count: " + threadCount);
//        assertTrue(threadCount > 0);
//    }
//
//    @Test
//    public void peakThreadCount() throws JSONException{
//        int peakThreadCount = this.dataProvider.peakThreadCount();
//        System.out.println("Peak Thread count: " + peakThreadCount);
//        assertTrue(peakThreadCount > 0);
//    }
//
//    @Test
//    public void totalErrors() throws JSONException{
//        int totalErrors = this.dataProvider.totalErrors();
//        System.out.println("Total errors: " + totalErrors);
//        assertTrue(totalErrors != -1);
//    }
//
//    @Test
//    public void currentThreadsBusy() throws JSONException{
//        int busyThreads = this.dataProvider.currentThreadBusy();
//        System.out.println("# of busy threads: " + busyThreads);
//        assertTrue(busyThreads != -1);
//    }
//
//    @Test
//    public void committedTX() throws JSONException{
//        int committedTX = this.dataProvider.committedTX();
//        System.out.println("# committedTX: " + committedTX);
//        assertTrue(committedTX != -1);
//    }
//
//    @Test
//    public void rolledBackTX() throws JSONException{
//        int rolledBackTX = this.dataProvider.rolledBackTX();
//        System.out.println("# rolledBackTX: " + rolledBackTX);
//        assertTrue(rolledBackTX != -1);
//    }
//
//    @Test
//    public void queuedConnections() throws JSONException{
//        int queuedConnections = this.dataProvider.queuedConnections();
//        System.out.println("# queuedConnections: " + queuedConnections);
//        assertTrue(queuedConnections != -1);
//    }
//
//    @Test
//    public void activeSessions() throws JSONException{
//        int activeSessions = this.dataProvider.activeSessionsCurrent();
//        System.out.println("# activeSessions: " + activeSessions);
//        assertTrue(activeSessions != -1);
//    }
//
//    @Test
//    public void expiredSessions() throws JSONException{
//        int expiredSessions = this.dataProvider.expiredSessions();
//        System.out.println("# expiredSessions: " + expiredSessions);
//        assertTrue(expiredSessions != -1);
//    }
//
//    @Test
//    public void applications() throws JSONException{
//        String[] applications = this.dataProvider.applications();
//        assertNotNull(applications);
//        System.out.println("# applications: " + toString(applications));
//        assertTrue(applications.length>0);
//    }
//    @Test
//    public void resources() throws JSONException{
//        String[] resources = this.dataProvider.resources();
//        assertNotNull(resources);
//        System.out.println("# resources: " + toString(resources));
//        assertTrue(resources.length>0);
//    }
//
//
//    @Test
//    public void getStringArray() throws JSONException{
//        String[] actual = this.dataProvider.getStringArray(SnapshotProvider.RESOURCES, "childResources");
//        assertNotNull(actual);
//        for (String slot : actual) {
//            assertNotNull(slot);
//            System.out.println(slot);
//        }
//
//    }
    private String toString(String[] applications) {
        String retVal = "";
        for (String string : applications) {
            retVal += string + " ";
        }
        return retVal;
    }
}
