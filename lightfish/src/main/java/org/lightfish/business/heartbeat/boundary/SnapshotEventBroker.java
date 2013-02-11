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
import org.lightfish.business.logging.Log;
import org.lightfish.business.monitoring.boundary.Severity;
import org.lightfish.business.monitoring.entity.Snapshot;
import org.lightfish.presentation.publication.BrowserWindow;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.inject.Instance;
import javax.xml.bind.annotation.XmlRootElement;
import org.lightfish.business.escalation.entity.Escalation;
import org.lightfish.presentation.publication.escalation.EscalationWindow;
import org.lightfish.presentation.publication.escalation.Escalations;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class SnapshotEventBroker {

    private ConcurrentLinkedQueue<BrowserWindow> browsers = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<BrowserWindow> escalationBrowsers = new ConcurrentLinkedQueue<>();
    private ConcurrentHashMap<String, Escalation> escalations = new ConcurrentHashMap<>();
    @Inject
    Log LOG;
    @Inject
    Serializer serializer;
    @Resource
    TimerService timerService;
    private Timer timer;
    @Inject
    private Instance<Integer> interval;

    @PostConstruct
    public void startTimer() {
        ScheduleExpression expression = new ScheduleExpression();
        expression.minute("*").second("*/" + interval.get()).hour("*");
        this.timer = this.timerService.createCalendarTimer(expression);
    }

    public void onBrowserRequest(@Observes BrowserWindow browserWindow) {
        LOG.info("Added " + browserWindow.hashCode());
        browsers.add(browserWindow);
    }
    
    public void onEscalationBrowserRequest(@Observes @EscalationWindow BrowserWindow browserWindow) {
        escalationBrowsers.add(browserWindow);
    }

    public void onNewSnapshot(@Observes @Severity(Severity.Level.HEARTBEAT) Snapshot snapshot) {
        for (BrowserWindow browserWindow : browsers) {
            if (browserWindow.getChannel() == null) {
                try {
                    send(browserWindow, snapshot);
                    LOG.info("Sent to " + browserWindow.hashCode());
                } finally {
                    browsers.remove(browserWindow);
                }
            }
        }
    }

    public void onNewEscalation(@Observes @Severity(Severity.Level.ESCALATION) Escalation escalated) {
        this.escalations.put(escalated.getChannel(), escalated);
    }

    @Timeout
    public void notifyEscalationListeners() {
        for (BrowserWindow browserWindow : escalationBrowsers) {
            String channel = browserWindow.getChannel();
            try {
                if (channel != null && !channel.isEmpty()) {
                    Escalation snapshot = this.escalations.get(channel);

                    if (snapshot != null) {
                        send(browserWindow, snapshot);
                    } else {
                        browserWindow.nothingToSay();
                    }

                } else {
                    if (!this.escalations.isEmpty()) {
                        send(browserWindow, new Escalations(this.escalations));
                    } else {
                        browserWindow.nothingToSay();
                    }
                }
            } finally {
                escalationBrowsers.remove(browserWindow);
            }
        }
    }

    void send(BrowserWindow browserWindow, Snapshot snapshot) {
        Writer writer = browserWindow.getWriter();
        serializer.serialize(snapshot, writer);
        browserWindow.send();
    }
    
    void send(BrowserWindow browserWindow, Escalation escalation) {
        Writer writer = browserWindow.getWriter();
        serializer.serialize(escalation, writer);
        browserWindow.send();
    }

    void send(BrowserWindow browserWindow, Escalations snapshot) {
        Writer writer = browserWindow.getWriter();
        serializer.serialize(snapshot, writer);
        browserWindow.send();
    }
}
