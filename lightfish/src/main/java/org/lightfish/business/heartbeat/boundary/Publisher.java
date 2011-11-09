package org.lightfish.business.heartbeat.boundary;

import java.io.Writer;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.lightfish.business.heartbeat.control.Serializer;
import org.lightfish.business.monitoring.boundary.Severity;
import org.lightfish.business.monitoring.entity.Snapshot;
import org.lightfish.presentation.publication.BrowserWindow;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class Publisher {
    
    private CopyOnWriteArrayList<BrowserWindow> browserWindows = new CopyOnWriteArrayList<BrowserWindow>();
    
    @Inject
    Serializer serializer;
    
    public void onBrowserRequest(@Observes BrowserWindow browserWindow){
        browserWindows.add(browserWindow);
    }
    
    public void onNewSnapshot(@Observes @Severity(Severity.Level.HEARTBEAT) Snapshot snapshot){
        for (BrowserWindow browserWindow : browserWindows) {
            Writer writer = browserWindow.getWriter();
            serializer.serialize(snapshot, writer);
            browserWindow.send();
            browserWindows.remove(browserWindow);
        }
    }
    
}
