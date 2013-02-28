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
package org.lightfish.business.heartbeat.boundary;

import org.lightfish.business.heartbeat.control.Serializer;
import org.lightfish.business.monitoring.boundary.Severity;
import org.lightfish.business.monitoring.entity.Snapshot;
import org.lightfish.presentation.publication.BrowserWindow;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Lock;
import javax.ejb.LockType;
import org.lightfish.business.monitoring.control.collectors.SnapshotDataCollector;
import org.lightfish.presentation.publication.AsyncMultiWriter;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class SnapshotEventBroker {

    private ConcurrentLinkedQueue<BrowserWindow> browsers = new ConcurrentLinkedQueue<>();
    @Inject
    Logger LOG;
    @Inject
    Serializer serializer;

    public void onBrowserRequest(@Observes @SnapshotDataCollector BrowserWindow browserWindow) {
        LOG.log(Level.FINE, "Added {0} to watch channel {1}", new Object[]{browserWindow.hashCode(), browserWindow.getChannel()});
        browsers.add(browserWindow);
    }

    @Lock(LockType.WRITE)
    public void onNewSnapshot(@Observes @Severity(Severity.Level.HEARTBEAT) Snapshot snapshot) {
        List<BrowserWindow> currentBrowsers = new ArrayList<>(browsers);
        List<BrowserWindow> staging = new ArrayList<>(browsers.size());
        AsyncMultiWriter multiWriter = new AsyncMultiWriter();
        for (BrowserWindow browserWindow : currentBrowsers) {
            if(browserWindow.getChannel()==null || browserWindow.getChannel().trim().isEmpty()){
                LOG.log(Level.INFO, "Found a browser window({0}) with no channel", browserWindow.hashCode());
            }
            if (snapshot.getInstanceName().equals(browserWindow.getChannel())) {
                LOG.log(Level.FINEST, "Staging {0}", browserWindow.hashCode());
                multiWriter.addWriter(browserWindow.getWriter());
                staging.add(browserWindow);
            }
        }
        
        browsers.removeAll(staging);
        
        LOG.finest("Serializing snapshot to all staged browser windows");
        serializer.serialize(snapshot, multiWriter);
        
        for(BrowserWindow browserWindow:staging){
            LOG.log(Level.FINEST, "Sending {0}", browserWindow.hashCode());
            browserWindow.send();
        }
    }

    void send(BrowserWindow browserWindow, Snapshot snapshot) {
        Writer writer = browserWindow.getWriter();
        serializer.serialize(snapshot, writer);
        browserWindow.send();
    }
}
