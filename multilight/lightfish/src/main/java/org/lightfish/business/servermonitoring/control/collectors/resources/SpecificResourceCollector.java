package org.lightfish.business.servermonitoring.control.collectors.resources;

import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;
import org.lightfish.business.servermonitoring.entity.ConnectionPool;

/**
 *
 * @author Rob Veldpaus
 */
public interface SpecificResourceCollector {

    static final String RESOURCES = "resources";

    public static ConnectionPool apply(RestDataCollector collector, String serverInstance, String resourceName) {
        Response clientResponse = collector.getResponse(serverInstance, constructResourceString(resourceName));
        JsonObject response = clientResponse.readEntity(JsonObject.class);
        JsonObject entity = response.getJsonObject("extraProperties").
                getJsonObject("entity");

        int numconnfree = getIntVal(entity, "numconnfree", "current");
        int numconnused = getIntVal(entity, "numconnused", "current");
        int waitqueuelength = getIntVal(entity, "waitqueuelength", "count");
        int numpotentialconnleak = getIntVal(entity, "numpotentialconnleak", "count");

        return new ConnectionPool(resourceName, numconnfree, numconnused, waitqueuelength, numpotentialconnleak);
    }

    static int getIntVal(JsonObject entity, String name, String key) {
        return entity.getJsonObject(name).getInt(key);
    }

    static String constructResourceString(String resourceName) {
        return RESOURCES + "/" + resourceName;
    }
}
