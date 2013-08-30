/*
 *
 */
package org.lightview.business.pool.boundary;

import javax.annotation.PostConstruct;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.lightview.business.pool.entity.PoolStatistics;

/**
 *
 * @author adam-bien.com
 */
public class EJBPoolMonitoring {

    private Client client;

    private String URI = "http://localhost:8080/lightfish/resources/applications/{application}/ejbs/{ejb}/pool";

    @PostConstruct
    public void init() {
        this.client = ClientBuilder.newClient();
    }

    public PoolStatistics getPoolStats(String application, String ejbName) {
        WebTarget target = this.client.target(URI);

        Response response = target.
                resolveTemplate("application", application).
                resolveTemplate("ejb", ejbName).
                request(MediaType.APPLICATION_JSON).get(Response.class);

        if (response.getStatus() == 404) {
            return null;
        }
        return new PoolStatistics(response.readEntity(JsonObject.class));
    }
}
