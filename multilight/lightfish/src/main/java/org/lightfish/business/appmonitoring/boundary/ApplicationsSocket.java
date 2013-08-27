package org.lightfish.business.appmonitoring.boundary;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author adam-bien.com
 */
@Singleton
@ServerEndpoint("/applications/{application-name}")
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class ApplicationsSocket {

    private ConcurrentMap<String, CopyOnWriteArraySet<Session>> sessions;

    @Inject
    ApplicationMonitoring applicationMonitoring;

    @PostConstruct
    public void init() {
        this.sessions = new ConcurrentHashMap<>();
    }

    @OnOpen
    public void onOpen(@PathParam("application-name") String name, Session session) {
        this.sessions.putIfAbsent(name, new CopyOnWriteArraySet<Session>());
        CopyOnWriteArraySet<Session> channel = this.sessions.get(name);
        channel.add(session);
    }

    @OnClose
    public void onClose(@PathParam("application-name") String name, Session session) {
        CopyOnWriteArraySet<Session> channel = this.sessions.get(name);
        channel.remove(session);
    }

    @Asynchronous
    public void distributeMessage() {
        for (Map.Entry<String, CopyOnWriteArraySet<Session>> entry : sessions.entrySet()) {
            String applicationName = entry.getKey();
            JsonObject beanStatistics = applicationMonitoring.getBeanStatistics(applicationName);
            CopyOnWriteArraySet<Session> value = entry.getValue();
            for (Session session : value) {
                if (session != null && session.isOpen()) {
                    session.getAsyncRemote().sendText(beanStatistics.toString());
                }

            }
        }
    }
}
