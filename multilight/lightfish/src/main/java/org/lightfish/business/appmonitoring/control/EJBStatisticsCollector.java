/*
 *
 */
package org.lightfish.business.appmonitoring.control;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author adam-bien.com
 */
public class EJBStatisticsCollector {

    @Inject
    Instance<String> location;

    @Inject
    Client client;

    public JsonObject fetchApplications() {
        JsonObject applications = client.target(getUri()).request(MediaType.APPLICATION_JSON).get(JsonObject.class);
        return preprocessChildResource(applications);
    }

    public JsonObject fetchApplicationComponents(String applicationName) {
        JsonObject applications = client.target(getUri()).path(applicationName).request(MediaType.APPLICATION_JSON).get(JsonObject.class);
        return preprocessChildResource(applications);
    }

    public JsonObject fetchMethods(String applicationName, String ejbName) {
        WebTarget target = client.target(getUri() + "{application}/{bean}/bean-methods/");
        Response response = target.resolveTemplate("application", applicationName).
                resolveTemplate("bean", ejbName).
                request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() == 404) {
            return null;
        }
        JsonObject rawStatistics = response.readEntity(JsonObject.class);
        return preprocessChildResource(rawStatistics);
    }

    public JsonObject fetchApplicationStatistics(String applicationName) {
        WebTarget target = client.target(getUri() + "{application}/server/");
        Response response = target.resolveTemplate("application", applicationName).
                request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() == 404) {
            return null;
        }
        JsonObject rawStatistics = response.readEntity(JsonObject.class);
        return preprocessEntity(rawStatistics);
    }

    public JsonObject fetchMethodStatistics(String applicationName, String ejbName, String methodName) {
        WebTarget target = client.target(getUri() + "{application}/{bean}/bean-methods/{method}");
        Response response = target.resolveTemplate("application", applicationName).
                resolveTemplate("bean", ejbName).
                resolveTemplate("method", methodName).
                request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() == 404) {
            return null;
        }
        JsonObject rawStatistics = response.readEntity(JsonObject.class);
        return preprocessEntity(rawStatistics);
    }

    public JsonObject fetchBeanPoolStatistics(String applicationName, String ejbName) {
        WebTarget target = client.target(getUri() + "{application}/{bean}/bean-pool");
        Response response = target.resolveTemplate("application", applicationName).
                resolveTemplate("bean", ejbName).
                request(MediaType.APPLICATION_JSON).get(Response.class);
        if (response.getStatus() == 404) {
            return null;
        }
        JsonObject rawStatistics = response.readEntity(JsonObject.class);
        return preprocessEntity(rawStatistics);
    }

    public String getUri() {
        return "http://" + location.get() + "/monitoring/domain/server/applications/";
    }

    JsonObject preprocessEntity(JsonObject entityResource) {
        if (isFailure(entityResource)) {
            return null;
        }
        JsonObject extraProperties = entityResource.getJsonObject("extraProperties");
        return extraProperties.getJsonObject("entity");
    }

    JsonObject preprocessChildResource(JsonObject childResource) {
        if (isFailure(childResource)) {
            return null;
        }
        JsonObject extraProperties = childResource.getJsonObject("extraProperties");
        return extraProperties.getJsonObject("childResources");
    }

    boolean isFailure(JsonObject object) {
        String exitCode = object.getString("exit_code");
        return ("FAILURE".equals(exitCode));
    }

}
