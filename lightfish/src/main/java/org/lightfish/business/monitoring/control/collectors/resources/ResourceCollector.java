package org.lightfish.business.monitoring.control.collectors.resources;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.lightfish.business.monitoring.control.collectors.paranormal.*;
import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.monitoring.entity.ConnectionPool;

/**
 *
 * @author rveldpau
 */
public class ResourceCollector extends AbstractRestDataCollector<List<ConnectionPool>> {

    private static final String RESOURCES = "resources";

    @Override
    public DataPoint<List<ConnectionPool>> collect() throws Exception {
        String[] resourceNames = resources();
        List<ConnectionPool> resources = new ArrayList<>(resourceNames.length);
        for (String jdbcPoolName : resourceNames) {
            resources.add(fetchResource(jdbcPoolName));
        }
        return new DataPoint<>("resources",resources);
    }

    private String[] resources() throws JSONException {
        return getStringArray(RESOURCES, "childResources");
    }
    
    private ConnectionPool fetchResource(String jndiName) throws Exception {
        int numconnfree = numconnfree(jndiName);
        int waitqueuelength = waitqueuelength(jndiName);
        int numpotentialconnleak = numpotentialconnleak(jndiName);
        int numconnused = numconnused(jndiName);
        return new ConnectionPool(jndiName, numconnfree, numconnused, waitqueuelength, numpotentialconnleak);
    }

    private int numconnfree(String jndiName) throws JSONException {
        String uri = constructResourceString(jndiName);
        return getInt(uri, "numconnfree", "current");
    }

    private int numconnused(String jndiName) throws JSONException {
        String uri = constructResourceString(jndiName);
        return getInt(uri, "numconnused", "current");
    }

    private int waitqueuelength(String jndiName) throws JSONException {
        String uri = constructResourceString(jndiName);
        return getInt(uri, "waitqueuelength");
    }

    private int numpotentialconnleak(String jndiName) throws JSONException {
        String uri = constructResourceString(jndiName);
        return getInt(uri, "numpotentialconnleak");
    }

    private String constructResourceString(String resourceName) {
        return RESOURCES + "/" + resourceName;
    }
}
