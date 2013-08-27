/*
 *
 */
package org.lightfish.business.appmonitoring.boundary;

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

    @Override
    public void onOpen(Session session, EndpointConfig ec) {
        System.out.println("Opening session: " + session);
        session.addMessageHandler(new MessageHandler.Whole<String>() {

            public void onMessage(String message) {
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
