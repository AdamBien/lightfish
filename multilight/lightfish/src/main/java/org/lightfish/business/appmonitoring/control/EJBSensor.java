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

/**
 *
 * @author adam-bien.com
 */
public class EJBSensor {

    @Inject
    Instance<String> location;

    @Inject
    Client client;

    public JsonObject fetch(String applicationName, String ejbName, String methodName) {
        WebTarget target = client.target(getUri() + "{application}/{bean}/bean-methods/{method}");
        JsonObject rawStatistics = target.resolveTemplate("application", applicationName).
                resolveTemplate("bean", ejbName).
                resolveTemplate("method", methodName).
                request(MediaType.APPLICATION_JSON).get(JsonObject.class);

        return preprocess(rawStatistics);
    }

    public String getUri() {
        return "http://" + location.get() + "/monitoring/domain/server/applications/";
    }

    JsonObject preprocess(JsonObject statistics) {
        JsonObject extraProperties = statistics.getJsonObject("extraProperties");
        return extraProperties.getJsonObject("entity");
    }

}
