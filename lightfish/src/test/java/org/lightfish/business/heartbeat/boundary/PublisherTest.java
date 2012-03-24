package org.lightfish.business.heartbeat.boundary;

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
public class PublisherTest {
    
    private Publisher cut;
    
    @Before
    public void init(){
        this.cut = new Publisher();
        this.cut.serializer = mock(Serializer.class);
        this.cut.LOG = new Log();
    }


    @Test
    public void singleChannelNofified() {
        final String escalationChannel = "duke";
        BrowserWindow window = mock(BrowserWindow.class);
        Snapshot snapshot = new Snapshot();
        snapshot.setEscalationChannel(escalationChannel);
        when(window.getChannel()).thenReturn(escalationChannel);
        this.cut.onBrowserRequest(window);
        this.cut.onNewEscalation(snapshot);
        verify(window,never()).send();
        this.cut.notifyEscalationListeners();
        verify(window).send();
    }

    @Test
    public void separationOfNotificationsAndEscalations() {
        final String escalationChannel = "duke";
        BrowserWindow window = mock(BrowserWindow.class);
        Snapshot snapshot = new Snapshot();
        snapshot.setEscalationChannel(escalationChannel);
        this.cut.onBrowserRequest(window);
        this.cut.onNewEscalation(snapshot);
        verify(window,never()).send();
        this.cut.notifyEscalationListeners();
        verify(window,never()).send();
    }
}
