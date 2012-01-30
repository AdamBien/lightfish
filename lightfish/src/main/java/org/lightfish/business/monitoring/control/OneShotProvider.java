package org.lightfish.business.monitoring.control;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.lightfish.business.monitoring.entity.OneShot;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

/**
 * User: blog.adam-bien.com
 * Date: 30.01.12
 * Time: 19:40
 */
public class OneShotProvider {
    protected Client client;
    protected String baseManagementUri;
    protected String baseMonitoringUri;

    @Inject
    String location;

    private WebResource managementResource;

    @PostConstruct
    public void initializeClient() {
        this.client = Client.create();
        this.baseMonitoringUri = "http://" + location + "/monitoring/domain/server/";
        this.baseManagementUri = "http://" + location + "/management/domain/";
        this.managementResource = this.client.resource(this.baseManagementUri);
    }


    String getVersion() throws JSONException {
        JSONObject result = this.managementResource.path("version").accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
        return result.getString("message");
    }


    public OneShot fetchOneShot(){
        String version = null;
        try {
            version = getVersion();
        } catch (JSONException e) {
            throw new IllegalStateException("Cannot fetch static monitoring data because of: " + e);
        }
        return new OneShot.Builder().version(version).build();
    }

}
