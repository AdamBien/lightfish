package org.lightfish.business.heartbeat.boundary;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.lightfish.business.heartbeat.control.Serializer;
import org.lightfish.business.logging.Log;
import org.lightfish.business.servermonitoring.boundary.Severity;
import org.lightfish.business.servermonitoring.entity.Snapshot;

/**
 * @author: adam-bien.com
 */
@Singleton
@ServerEndpoint("/snapshots/json/")
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class SnapshotsSocketJSON {

    @Inject
    Log LOG;

    @Inject
    Serializer serializer;

    private CopyOnWriteArraySet<Session> sessions;

    @PostConstruct
    public void init() {
        this.sessions = new CopyOnWriteArraySet<>();
    }

    @OnOpen
    public void onOpen(Session session) {
        this.sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        this.sessions.remove(session);
    }

    public void onNewSnapshot(@Observes @Severity(Severity.Level.HEARTBEAT) Snapshot snapshot) {
        LOG.info("SnapshotsSocketJson.oneNewSnapshot: " + snapshot.getId());

        for (Session session : sessions) {
            if (session != null && session.isOpen()) {
                LOG.info("Sending: " + snapshot.getId() + " to " + session.getId());
                final RemoteEndpoint.Basic remote = session.getBasicRemote();
                try {
                    String json = snapshotToJson(snapshot).toString();
                    remote.sendText(json);
                    LOG.info("Sending Snapshot " + json);
                } catch (IOException ex) {
                    LOG.error("Problem sending Snapshot", ex);
                }
            }
        }
    }

    public JsonObject snapshotToJson(Snapshot snapshot) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("snapshot", toJson(snapshot));
        return objectBuilder.build();
    }

    public JsonObject toJson(Object object) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        Method methods[] = object.getClass().getDeclaredMethods();
        try {
            for (int i = 0; i < methods.length; i++) {
                String method = methods[i].getName();
                if (method.startsWith("get")) {
                    Object result = methods[i].invoke(object);
                    String property = method.replaceFirst("get", "");
                    property = Character.toLowerCase(property.charAt(0)) + property.substring(1);
                    if (methods[i].getReturnType().equals(List.class)) {
                        List resultList = (List) result;
                        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                        for (Object entry : resultList) {
                            if (entry instanceof String) {
                                arrayBuilder.add((String) entry);
                            } else {
                                JsonObject toJson = toJson(entry);
                                arrayBuilder.add(toJson);
                            }
                        }
                        objectBuilder.add(property, arrayBuilder.build());
                    } else {
                        objectBuilder.add(property, "" + result);
                    }
                }

            }
            return objectBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
