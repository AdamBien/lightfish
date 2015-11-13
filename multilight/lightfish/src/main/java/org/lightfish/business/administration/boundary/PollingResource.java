/*
 *
 */
package org.lightfish.business.administration.boundary;

import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.lightfish.business.configuration.boundary.Configurator;
import org.lightfish.business.servermonitoring.boundary.MonitoringAdmin;
import org.lightfish.business.servermonitoring.boundary.MonitoringController;

/**
 *
 * @author adam-bien.com
 */
@Path("polling")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class PollingResource {

    public static final String INTERVAL = "interval";
    public static final String LOCATION = "location";

    @Inject
    MonitoringController controller;
    @Inject
    Configurator configurator;
    @Inject
    MonitoringAdmin admin;

    @POST
    public JsonObject startPolling(JsonObject pollingInfo) {
        int interval = pollingInfo.getInt(INTERVAL);
        configurator.setValue(INTERVAL, interval);
        String location = pollingInfo.getString(LOCATION, "localhost:4848");
        configurator.setValue(LOCATION, location);
        controller.restart();
        return status();

    }

    @GET
    public JsonObject status() {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        Date nextTimeout = controller.nextTimeout();
        String timeoutAsString = "-";
        if (nextTimeout != null) {
            timeoutAsString = nextTimeout.toString();
        }
        objectBuilder.add("nextTimeout", timeoutAsString).add(INTERVAL, configurator.getValue(INTERVAL));
        return objectBuilder.build();
    }

    @DELETE
    public void stopPolling() {
        controller.stopTimer();
    }
}
