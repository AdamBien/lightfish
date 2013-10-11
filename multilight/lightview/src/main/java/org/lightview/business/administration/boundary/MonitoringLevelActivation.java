package org.lightview.business.administration.boundary;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.lightview.presentation.dashboard.DashboardModel;

/**
 * @author: adam-bien.com
 */
public class MonitoringLevelActivation {

    private Client client;

    @Inject
    DashboardModel model;

    @PostConstruct
    public void init() {
        this.client = ClientBuilder.newClient();
    }

    public boolean activateMonitoring() {
        Response response = getAdministrationTarget().request().post(null);
        return response.getStatus() == 204;
    }

    public boolean deactivateMonitoring() {
        Response response = getAdministrationTarget().request().delete();
        return response.getStatus() == 204;
    }

    private WebTarget getAdministrationTarget() {
        return this.client.target(getUri());
    }

    public String getUri() {
        return model.serverUriProperty().get() + "/resources/monitoring";
    }

}
