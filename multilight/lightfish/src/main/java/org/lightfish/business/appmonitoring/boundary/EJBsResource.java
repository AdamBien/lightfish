/*
 *
 */
package org.lightfish.business.appmonitoring.boundary;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author adam-bien.com
 */
public class EJBsResource {

    @Inject
    ApplicationMonitoring am;

    @GET
    @Path("{ejb-name}")
    public JsonObject getEJBStatistics(@PathParam("application-name") String applicationName, @PathParam("ejb-name") String ejbName) {
        return am.getBeanStatistics(applicationName, ejbName);
    }

    @GET
    @Path("{ejb-name}/pool")
    public JsonObject getEJBPoolStatistics(@PathParam("application-name") String applicationName, @PathParam("ejb-name") String ejbName) {
        return am.getBeanPoolStatistics(applicationName, ejbName);
    }

    @GET
    public JsonObject getEJBStatistics(@PathParam("application-name") String applicationName) {
        System.out.println("Application name: " + applicationName);
        return am.getBeanStatistics(applicationName);
    }

}
