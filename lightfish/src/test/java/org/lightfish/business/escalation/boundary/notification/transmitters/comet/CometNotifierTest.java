package org.lightfish.business.escalation.boundary.notification.transmitters.comet;

import org.lightfish.business.escalation.boundary.notification.transmitters.comet.CometTransmitterDelegate;
import java.io.Writer;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.lightfish.business.escalation.entity.Escalation;
import org.lightfish.business.heartbeat.boundary.SnapshotEventBroker;
import org.lightfish.business.heartbeat.control.Serializer;
import org.lightfish.business.logging.Log;
import org.lightfish.business.monitoring.entity.Snapshot;
import org.lightfish.presentation.publication.BrowserWindow;
import static org.mockito.Mockito.*;
/**
 *
 * @author adam bien, adam-bien.com
 */
public class CometNotifierTest {
    
    private CometTransmitterDelegate cut;
    
    @Before
    public void init(){
        this.cut = new CometTransmitterDelegate();
        this.cut.serializer = mock(Serializer.class);
    }


    @Test
    public void singleChannelNofified() {
        final String escalationChannel = "duke";
        BrowserWindow window = mock(BrowserWindow.class);
        
        Writer writer = mock(Writer.class);
        when(window.getWriter()).thenReturn(writer);
        
        Escalation escalation = new Escalation();
        escalation.setChannel(escalationChannel);
        when(window.getChannel()).thenReturn(escalationChannel);
        
        this.cut.onEscalationBrowserRequest(window);
        this.cut.addEscalation(escalation);
        verify(window,never()).send();
        this.cut.notifyEscalationListeners();
        verify(window).send();
    }

    /**
     * Without specifying a Channel for a window, or specifying "" as the channel,
     * the "broadcast" channel should be notified.
     */
    @Test
    public void broadcastChannelNofified() {
        final String escalationChannel = "duke";
        BrowserWindow window = mock(BrowserWindow.class);
        when(window.getChannel()).thenReturn("");
        Escalation escalation = new Escalation();
        escalation.setChannel(escalationChannel);
        this.cut.onEscalationBrowserRequest(window);
        this.cut.addEscalation(escalation);
        verify(window,never()).send();
        this.cut.notifyEscalationListeners();
        verify(window).send();
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
