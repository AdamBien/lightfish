/*
 *
 */
package org.lightfish.business.appmonitoring.boundary;

import java.util.concurrent.CountDownLatch;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

/**
 *
 * @author adam-bien.com
 */
public class MessageEndpoint extends Endpoint {

    private String message;

    private CountDownLatch latch;

    public MessageEndpoint(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onOpen(Session session, EndpointConfig ec) {
        System.out.println("Opening session: " + session);
        session.addMessageHandler(new MessageHandler.Whole<String>() {

            public void onMessage(String message) {
                latch.countDown();
                setMessage(message);
            }
        });
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
