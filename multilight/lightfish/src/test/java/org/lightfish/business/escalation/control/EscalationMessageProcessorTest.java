package org.lightfish.business.escalation.control;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.lightfish.business.servermonitoring.entity.ConnectionPool;
import org.lightfish.business.servermonitoring.entity.Snapshot;
import static org.mockito.Mockito.mock;

/**
 *
 * @author rveldpau
 */
public class EscalationMessageProcessorTest {

    EscalationMessageProcessor instance;

    @Before
    public void setUp() {
        instance = new EscalationMessageProcessor();
        instance.init();
        instance.LOG = Logger.getGlobal();
    }

    @Test
    public void basic_replace_nothing() throws Exception {
        String expected = "This is a test";
        Snapshot escalation = mock(Snapshot.class);
        String result = instance.processBasicMessage(expected, escalation);
        assertEquals(expected, result);
    }

    @Test
    public void basic_replace_heap() throws Exception {
        String expected = "Used Heap: 42";
        String template = "Used Heap: ${usedHeapSize}";
        Snapshot snapshot = new Snapshot.Builder().usedHeapSize(42).build();
        String result = instance.processBasicMessage(template, snapshot);
        assertEquals(expected, result);
    }

    @Test
    public void basic_subvalue_replace() throws Exception {
        String expected = "Free Connections: 42";
        String template = "Free Connections: ${pools[0].numconnfree}";
        List<ConnectionPool> pools = new ArrayList<>();
        pools.add(new ConnectionPool("anything", 42, 24, 1, 1));

        Snapshot snapshot = new Snapshot.Builder().pools(pools).build();
        String result = instance.processBasicMessage(template, snapshot);
        assertEquals(expected, result);
    }

    /*
     * The following test does not run properly although it is valid. There appear
     * to be dependency issues for PegDown. If you switch to PegDown 1.1.0 this
     * test runs fine, however, the application when deployed in Glassfish fails.
     */
    @Test
    @Ignore
    public void rich_replace() throws Exception {
        String expected = "<h3>Free Connections</h3>\n"
                + "<pre><code>42\n"
                + "</code></pre>";
        String template = "### Free Connections\n    ${pools[0].numconnfree}";
        List<ConnectionPool> pools = new ArrayList<>();
        pools.add(new ConnectionPool("anything", 42, 24, 1, 1));

        Snapshot snapshot = new Snapshot.Builder().pools(pools).build();
        String result = instance.processRichMessage(template, snapshot);
        System.out.println(result);
        assertEquals(expected, result);
    }

}
