package org.lightfish.business.monitoring.control;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.lightfish.business.monitoring.entity.OneShot;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

/**
 * User: blog.adam-bien.com
 * Date: 30.01.12
 * Time: 19:40
 */
public class OneShotProvider {
    protected Client client;

    @Inject
    Instance<String> location;

    private WebResource managementResource;

    @PostConstruct
    public void initializeClient() {
        this.client = Client.create();
    }


    String getVersion() throws JSONException {
        this.managementResource = this.client.resource(getManagementURI());
        JSONObject result = getJSONObject("version");
        return result.getString("message");
    }

    String getUpTime() throws JSONException {
        this.managementResource = this.client.resource(getManagementURI());
        JSONObject result = getJSONObject("uptime");
        return result.getString("message");
    }

    JSONObject getJSONObject(String name) throws UniformInterfaceException {
        JSONObject result = this.managementResource.path(name).accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
        return result;
    }
    
    String getMonitoringURI(){
        return "http://" + location.get() + "/monitoring/domain/server/";
    }
    String getManagementURI(){
        return "http://" + location.get() + "/management/domain/";
    }

    public OneShot fetchOneShot(){
        String version = null;
        String uptime = null;
        try {
            version = getVersion();
            uptime = getUpTime();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot fetch static monitoring data because of: " + e);
        }
        return new OneShot.Builder().version(version).uptime(uptime).build();
    }

}
