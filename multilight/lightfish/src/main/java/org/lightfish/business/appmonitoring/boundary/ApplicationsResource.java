/*
 *
 */
package org.lightfish.business.appmonitoring.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;

/**
 *
 * @author adam-bien.com
 */
@Stateless
@Path("applications")
public class ApplicationsResource {

    @Inject
    ApplicationMonitoring am;

    @Context
    ResourceContext context;

    @GET
    public JsonObject applications() {
        return am.getApplicationsContainerStatistics();
    }

    @GET
    @Path("{application-name}")
    public JsonObject application(@PathParam("application-name") String name) {
        return am.getApplicationContainerStatistics(name);
    }

    @Path("{application-name}/ejbs")
    public EJBsResource getEJB() {
        return context.getResource(EJBsResource.class);
    }
}
