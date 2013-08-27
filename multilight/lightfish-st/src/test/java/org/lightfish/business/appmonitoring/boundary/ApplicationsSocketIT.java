/*
 *
 */
package org.lightfish.business.appmonitoring.boundary;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class ApplicationsSocketIT {

    @Before
    public void init() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://echo.websocket.org:80/";
        System.out.println("Connecting to " + uri);
        container.connectToServer(MyClientEndpoint.class, URI.create(uri));
        messageLatch.await(100, TimeUnit.SECONDS);
    }

    @Test
    public void testSomeMethod() {
    }

}
