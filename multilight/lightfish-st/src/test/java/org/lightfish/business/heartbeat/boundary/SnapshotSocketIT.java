/*
 *
 */
package org.lightfish.business.heartbeat.boundary;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.lightfish.business.MessageEndpoint;

/**
 *
 * @author adam-bien.com
 */
public class SnapshotSocketIT {

    private MessageEndpoint endpoint;
    private CountDownLatch latch;

    @Before
    public void init() throws DeploymentException, IOException {
        this.latch = new CountDownLatch(1);
        this.endpoint = new MessageEndpoint(this.latch);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();
        String uri = "ws://localhost:8080/lightfish/snapshots/";
        System.out.println("Connecting to " + uri);
        Session session = container.connectToServer(this.endpoint, config, URI.create(uri));
    }

    /**
     * Setup updates to 2 seconds before performing this test
     */
    @Test
    public void statisticsArrived() throws InterruptedException {
        assertTrue(this.latch.await(5, TimeUnit.SECONDS));
        String message = endpoint.getMessage();
        assertNotNull(message);
        System.out.println("Message: " + message);
    }

}
