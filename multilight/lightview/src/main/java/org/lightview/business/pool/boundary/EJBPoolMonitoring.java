/*
 *
 */
package org.lightview.business.pool.boundary;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.lightview.business.pool.entity.PoolStatistics;
import org.lightview.presentation.dashboard.DashboardModel;

/**
 *
 * @author adam-bien.com
 */
public class EJBPoolMonitoring {

    private Client client;

    @Inject
    DashboardModel model;

    @PostConstruct
    public void init() {
        this.client = ClientBuilder.newClient();
    }

    public PoolStatistics getPoolStats(String application, String ejbName) {
        final String uri = getUri();
        System.out.println("Uri: " + uri);
        WebTarget target = this.client.target(uri);

        Response response = target.
                resolveTemplate("application", application).
                resolveTemplate("ejb", ejbName).
                request(MediaType.APPLICATION_JSON).get(Response.class);

        if (response.getStatus() == 404) {
            return null;
        }
        return new PoolStatistics(response.readEntity(JsonObject.class));
    }

    public String getUri() {
        return model.serverUriProperty().get() + "/resources/applications/{application}/ejbs/{ejb}/pool";
    }
}
