/*
 *
 */
package org.lightfish.business.administration.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.lightfish.business.servermonitoring.boundary.MonitoringAdmin;

/**
 *
 * @author adam-bien.com
 */
@Path("monitoring")
@Stateless
public class MonitoringResource {

    @Inject
    MonitoringAdmin monitoringAdmin;

    @POST
    public void activate() {
        monitoringAdmin.activateMonitoring();
    }

    @DELETE
    public void deactivate() {
        monitoringAdmin.deactivateMonitoring();
    }

}
