package org.lightfish.monitoring.control;

import org.lightfish.monitoring.control.DataProvider;
import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class DataProviderIT {
    
    DataProvider dataProvider;
    
    @Before
    public void initialize(){
        this.dataProvider = new DataProvider();
        this.dataProvider.initializeClient();
    }

    @Test
    public void fetchHeap() throws JSONException {
        long usedHeapSize = this.dataProvider.usedHeapSize();
        System.out.println("Used heap size: " + usedHeapSize);
        assertTrue(usedHeapSize > 0);
    }
    
    @Test
    public void threadCount() throws JSONException{
        int threadCount = this.dataProvider.threadCount();
        System.out.println("Thread count: " + threadCount);
        assertTrue(threadCount > 0);
    }
    
    @Test
    public void totalErrors() throws JSONException{
        int totalErrors = this.dataProvider.totalErrors();
        System.out.println("Total errors: " + totalErrors);
        assertTrue(totalErrors != -1);
    }

    @Test
    public void currentThreadsBusy() throws JSONException{
        int busyThreads = this.dataProvider.currentThreadBusy();
        System.out.println("# of busy threads: " + busyThreads);
        assertTrue(busyThreads != 0);
    }

    @Test
    public void committedTX() throws JSONException{
        int committedTX = this.dataProvider.committedTX();
        System.out.println("# committedTX: " + committedTX);
        assertTrue(committedTX != -1);
    }

    @Test
    public void rolledBackTX() throws JSONException{
        int rolledBackTX = this.dataProvider.rolledBackTX();
        System.out.println("# rolledBackTX: " + rolledBackTX);
        assertTrue(rolledBackTX != -1);
    }
    
    @Test
    public void queuedConnections() throws JSONException{
        int queuedConnections = this.dataProvider.queuedConnections();
        System.out.println("# queuedConnections: " + queuedConnections);
        assertTrue(queuedConnections != -1);
    
    }
}
