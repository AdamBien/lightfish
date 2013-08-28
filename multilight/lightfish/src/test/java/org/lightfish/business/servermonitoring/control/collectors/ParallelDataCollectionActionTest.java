package org.lightfish.business.servermonitoring.control.collectors;

import org.lightfish.business.servermonitoring.control.collectors.DataCollector;
import org.lightfish.business.servermonitoring.control.collectors.ParallelDataCollectionAction;
import org.lightfish.business.servermonitoring.control.collectors.DataPoint;
import java.util.concurrent.Future;
import javax.enterprise.inject.Instance;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;

/**
 *
 * @author rveldpau
 */
public class ParallelDataCollectionActionTest {

    ParallelDataCollectionAction action;

    @Before
    public void setUp() {
        action = new ParallelDataCollectionAction();
        action.dataCollectionRetries = mock(Instance.class);
        action.LOG = mock(java.util.logging.Logger.class);
        when(action.dataCollectionRetries.get()).thenReturn(1);
    }

    @Test
    public void action_all_good() throws Exception {
        DataCollector collector = mock(DataCollector.class);
        DataPoint expectedResult = mock(DataPoint.class);
        when(collector.collect()).thenReturn(expectedResult);
        Future compute = action.compute(collector);
        assertEquals(expectedResult, compute.get());
    }

    @Test
    public void action_with_retry() throws Exception {
        DataCollector collector = mock(DataCollector.class);
        DataPoint expectedResult = mock(DataPoint.class);
        when(collector.collect())
                .thenThrow(new Exception("Ahh!!"))
                .thenReturn(expectedResult);
        Future compute = action.compute(collector);
        assertEquals(expectedResult, compute.get());
    }

    @Test
    public void action_that_fails() throws Exception {
        DataCollector collector = mock(DataCollector.class);
        when(collector.collect())
                .thenThrow(new Exception("Ahh!!"))
                .thenThrow(new Exception("Another Ahh!!"));
        Future compute = action.compute(collector);
        assertNull(compute.get());
        assertEquals("Another Ahh!!", action.getThrownException().getMessage());
    }
}