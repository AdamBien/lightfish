package org.lightfish.business.monitoring.control.collectors.resources;

import com.sun.jersey.api.client.ClientResponse;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.monitoring.entity.ConnectionPool;

/**
 *
 * @author Rob Veldpaus
 */
@ResourceDataCollector
public class SpecificResourceCollector extends AbstractRestDataCollector<ConnectionPool> {

    private static final String RESOURCES = "resources";
    
    private String resourceName;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
    @Override
    public DataPoint<ConnectionPool> collect() throws Exception {
        ClientResponse clientResponse = getClientResponse(constructResourceString());
        JSONObject response = clientResponse.getEntity(JSONObject.class);
        JSONObject entity = response.getJSONObject("extraProperties").
                getJSONObject("entity");
        
        int numconnfree = getIntVal(entity,"numconnfree","current");
        int waitqueuelength = getIntVal(entity, "numconnused", "current");
        int numpotentialconnleak = getIntVal(entity, "waitqueuelength", "count");
        int numconnused = getIntVal(entity, "numpotentialconnleak", "count");
        
        return new DataPoint<>(resourceName,new ConnectionPool(resourceName, numconnfree, numconnused, waitqueuelength, numpotentialconnleak));
    }

    private int getIntVal(JSONObject entity, String name, String key) throws Exception{
        return entity.getJSONObject(name).getInt(key);
    }
    
    private String constructResourceString() {
        return RESOURCES + "/" + resourceName;
    }
}
