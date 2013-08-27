/*
 *
 */
package org.lightfish.business.appmonitoring.boundary;

import java.io.IOException;
import java.net.URI;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class ApplicationsSocketIT {

    private MessageEndpoint endpoint;

    @Before
    public void init() throws DeploymentException, IOException {
        this.endpoint = new MessageEndpoint();
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();
        String uri = "ws://localhost:8080/lightfish/applications/lightfish";
        System.out.println("Connecting to " + uri);
        Session session = container.connectToServer(this.endpoint, config, URI.create(uri));
    }

    /**
     * Setup updates to 2 seconds before performing this test
     */
    @Test(timeout = 5000)
    public void statisticsArrived() throws InterruptedException {
        Thread.sleep(3000);
        String message = endpoint.getMessage();
        assertNotNull(message);
        System.out.println("Message: " + message);
    }

}
