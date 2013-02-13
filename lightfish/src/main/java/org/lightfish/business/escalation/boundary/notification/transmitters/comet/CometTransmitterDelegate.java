package org.lightfish.business.escalation.boundary.notification.transmitters.comet;

import java.io.Writer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.lightfish.business.escalation.boundary.notification.EscalationNotificationBroker;
import org.lightfish.business.escalation.entity.Notifier;
import org.lightfish.business.escalation.boundary.notification.NotifierStore;
import org.lightfish.business.escalation.boundary.notification.transmitter.Transmitter;
import org.lightfish.business.escalation.boundary.notification.transmitter.TransmitterType;
import org.lightfish.business.escalation.entity.Escalation;
import org.lightfish.business.heartbeat.control.Serializer;
import org.lightfish.presentation.publication.BrowserWindow;
import org.lightfish.presentation.publication.escalation.EscalationWindow;
import org.lightfish.presentation.publication.escalation.Escalations;

/**
 *
 * @author rveldpau
 */
@Singleton
@Startup
public class CometTransmitterDelegate {

    public static Logger LOG = Logger.getLogger(CometTransmitterDelegate.class.getName());
    private ConcurrentLinkedQueue<BrowserWindow> escalationBrowsers = new ConcurrentLinkedQueue<>();
    private ConcurrentHashMap<String, Escalation> escalations = new ConcurrentHashMap<>();
    @Inject
    Serializer serializer;
    @Resource
    TimerService timerService;
    private Timer timer;
    @Inject
    private Instance<Integer> interval;
    @Inject
    NotifierStore notificationStore;

    @PostConstruct
    public void initialize() {
        startTimer();
        persistDefaultConfiguration();
    }

    private void startTimer() {
        ScheduleExpression expression = new ScheduleExpression();
        expression.minute("*").second("*/" + interval.get()).hour("*");
        this.timer = this.timerService.createCalendarTimer(expression);
    }

    private void persistDefaultConfiguration() {
        if (notificationStore.getNotifier("comet-escalator") == null) {
            Notifier configuration = new Notifier.Builder()
                    .name("comet-escalator")
                    .transmitterId("comet")
                    .configuration(new CometTransmitterConfiguration())
                    .build();
            notificationStore.save(configuration);
        }

    }

    public void addEscalation(Escalation escalation) {
        this.escalations.put(escalation.getChannel(), escalation);
    }

    public void onEscalationBrowserRequest(@Observes @EscalationWindow BrowserWindow browserWindow) {
        escalationBrowsers.add(browserWindow);
    }

    @Timeout
    public void notifyEscalationListeners() {
        for (BrowserWindow browserWindow : escalationBrowsers) {
            String channel = browserWindow.getChannel();
            try {
                if (channel != null && !channel.isEmpty()) {
                    Escalation snapshot = this.escalations.get(channel);

                    if (snapshot != null) {
                        sendToWindow(browserWindow, snapshot);
                    } else {
                        browserWindow.nothingToSay();
                    }

                } else {
                    if (!this.escalations.isEmpty()) {
                        sendToWindow(browserWindow, new Escalations(this.escalations));
                    } else {
                        browserWindow.nothingToSay();
                    }
                }
            } finally {
                escalationBrowsers.remove(browserWindow);
            }
        }
    }

    private void sendToWindow(BrowserWindow browserWindow, Escalation escalation) {
        Writer writer = browserWindow.getWriter();
        serializer.serialize(escalation, writer);
        browserWindow.send();
    }

    void sendToWindow(BrowserWindow browserWindow, Escalations escalations) {
        Writer writer = browserWindow.getWriter();
        serializer.serialize(escalations, writer);
        browserWindow.send();
    }
}
