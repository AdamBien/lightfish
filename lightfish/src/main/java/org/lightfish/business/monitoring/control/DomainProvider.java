package org.lightfish.business.monitoring.control;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import org.lightfish.business.monitoring.entity.Domain;

/**
 * User: Rob Veldpaus
 */
public class DomainProvider {

    protected Client client;
    @Inject
    Instance<String> location;
    @Inject
    Instance<String> username;
    @Inject
    Instance<String> password;
    @Inject
    Instance<GlassfishAuthenticator> authenticator;
    private WebResource managementResource;

    @PostConstruct
    public void initializeClient() {
        this.client = Client.create();
    }

    List<String> getInstances() throws JSONException {
        this.managementResource = this.client.resource(getInstancesUri());
        JSONObject serverResult = getJSONObject("server");
        JSONObject extraProperties = serverResult.getJSONObject("extraProperties");
        JSONObject result = extraProperties.getJSONObject("childResources");
        JSONArray names = result.names();
        List<String> instanceNames = new ArrayList<>();
        for (int i = 0; i < names.length(); i++) {
            instanceNames.add(names.getString(i));
        }
        return instanceNames;
    }

    JSONObject getJSONObject(String name) throws UniformInterfaceException {
        JSONObject result = this.managementResource.path(name).accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
        return result;
    }

    String getInstancesUri() {
        return getProtocol() + location.get() + "/management/domain/servers";
    }

    public Domain fetchDomainInfo() {
        authenticator.get().addAuthenticator(client, username.get(), password.get());
        List<String> servers = null;
        try {
            servers = getInstances();

        } catch (Exception e) {
            throw new IllegalStateException("Cannot fetch domain information because of: " + e);
        }
        return new Domain.Builder().instances(servers).build();
    }

    private String getProtocol() {
        String protocol = "http://";
        if (username != null && username.get() != null && !username.get().isEmpty()) {
            protocol = "https://";
        }
        return protocol;
    }
}
