package org.lightview.service;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectProperty;
import org.lightview.model.Snapshot;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * @author: adam-bien.com
 */
public class SnapshotEndpoint extends Endpoint {

    private SimpleObjectProperty<Snapshot> snapshot;
    private final Unmarshaller unmarshaller;
    private JAXBContext jaxb;

    public SnapshotEndpoint() {
        try {
            this.jaxb = JAXBContext.newInstance(Snapshot.class);
            this.unmarshaller = jaxb.createUnmarshaller();

        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot deserialize Snapshot", e);
        }
        this.snapshot = new SimpleObjectProperty<>();
    }

    @Override
    public void onOpen(Session session, EndpointConfig ec) {
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                final Snapshot current = deserialize(message);
                Platform.runLater(() ->
                        snapshot.set(current));
            }
        });
    }

    Snapshot deserialize(String message) {
        try {
            return (Snapshot) unmarshaller.unmarshal(new StringReader(message));
        } catch (JAXBException e) {
            throw new IllegalStateException("Cannot deserialize Snapshot", e);
        }
    }

    public ReadOnlyObjectProperty<Snapshot> snapshotProperty() {
        return snapshot;
    }
}
