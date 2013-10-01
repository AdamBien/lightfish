package org.lightview.service;

import javafx.beans.property.ReadOnlyObjectProperty;
import org.lightview.model.Snapshot;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

/**
 * @author: adam-bien.com
 */
public class SnapshotSocketListener {

    private SnapshotEndpoint endpoint;
    private Session session;

    @PostConstruct
    public void init() {
        this.endpoint = new SnapshotEndpoint();
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();
        String uri = "ws://localhost:8080/lightfish/snapshots/";
        System.out.println("Connecting to " + uri);
        try {
            session = container.connectToServer(this.endpoint, config, URI.create(uri));
        } catch (DeploymentException | IOException e) {
            throw new IllegalStateException("Cannot connect to WebSocket: ", e);
        }
    }

    public ReadOnlyObjectProperty<Snapshot> snapshotProperty() {
        return endpoint.snapshotProperty();
    }

    @PreDestroy
    public void disconnect() {
        try {
            session.close();
        } catch (IOException e) {
        }
    }
}
