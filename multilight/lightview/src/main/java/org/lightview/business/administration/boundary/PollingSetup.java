package org.lightview.business.administration.boundary;

import javafx.util.Pair;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.lightview.presentation.dashboard.DashboardModel;

/**
 * @author: adam-bien.com
 */
public class PollingSetup {

    private Client client;

    @Inject
    DashboardModel model;

    @PostConstruct
    public void init() {
        this.client = ClientBuilder.newClient();
    }

    public String changeInterval(String location, int newValue) {
        JsonObject interval = Json.createObjectBuilder().
                add("interval", newValue).
                add("location", location)
                .build();
        WebTarget administrationTarget = getAdministrationTarget();
        final Response response = administrationTarget.
                request(MediaType.APPLICATION_JSON).
                post(Entity.json(interval));
        return response.
                readEntity(JsonObject.class).
                getString("nextTimeout");

    }

    private WebTarget getAdministrationTarget() {
        return this.client.target(getUri());
    }

    public Pair<String, String> status() {
        Response response = getAdministrationTarget().
                request(MediaType.APPLICATION_JSON).
                get();
        JsonObject status = response.readEntity(JsonObject.class);
        String nextTimeout = status.getString("nextTimeout");
        String currentInterval = status.getString("interval");
        return new Pair<String, String>(currentInterval, nextTimeout);

    }

    public boolean stopPolling() {
        //stop
        Response response = getAdministrationTarget().request().delete();
        return (response != null && response.getStatus() == 204);
    }

    public String getUri() {
        return model.serverUriProperty().get() + "/resources/polling";
    }

}
