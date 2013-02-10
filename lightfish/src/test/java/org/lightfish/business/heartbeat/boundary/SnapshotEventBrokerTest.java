package org.lightfish.business.heartbeat.boundary;

import java.io.Writer;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.lightfish.business.heartbeat.control.Serializer;
import org.lightfish.business.logging.Log;
import org.lightfish.business.monitoring.entity.Snapshot;
import org.lightfish.presentation.publication.BrowserWindow;
import static org.mockito.Mockito.*;
/**
 *
 * @author adam bien, adam-bien.com
 */
public class SnapshotEventBrokerTest {
    
    private SnapshotEventBroker cut;
    
    @Before
    public void init(){
        this.cut = new SnapshotEventBroker();
        this.cut.serializer = mock(Serializer.class);
        this.cut.LOG = new Log();
    }


    @Test
    public void singleChannelNofified() {
        final String escalationChannel = "duke";
        BrowserWindow window = mock(BrowserWindow.class);
        
        Writer writer = mock(Writer.class);
        when(window.getWriter()).thenReturn(writer);
        
        Snapshot snapshot = new Snapshot();
        snapshot.setEscalationChannel(escalationChannel);
        when(window.getChannel()).thenReturn(escalationChannel);
        
        this.cut.onEscalationBrowserRequest(window);
        this.cut.onNewEscalation(snapshot);
        verify(window,never()).send();
        this.cut.notifyEscalationListeners();
        verify(window).send();
    }

    @Test
    public void separationOfNotificationsAndEscalations() {
        final String escalationChannel = "duke";
        BrowserWindow window = mock(BrowserWindow.class);
        when(window.getChannel()).thenReturn("not"+escalationChannel);
        Snapshot snapshot = new Snapshot();
        snapshot.setEscalationChannel(escalationChannel);
        this.cut.onEscalationBrowserRequest(window);
        this.cut.onNewEscalation(snapshot);
        verify(window,never()).send();
        this.cut.notifyEscalationListeners();
        verify(window,never()).send();
    }
    
    @Test
    public void nothingToSay(){
        BrowserWindow window = mock(BrowserWindow.class);
        when(window.getChannel()).thenReturn("does-not-matter");
        this.cut.onEscalationBrowserRequest(window);
        verify(window,never()).send();
        this.cut.notifyEscalationListeners();
        verify(window).nothingToSay();
        verify(window,never()).send();
    }
}
