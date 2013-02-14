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
import org.lightfish.business.monitoring.entity.ServerInstance;

/**
 * @author Rob Veldpaus
 */
public class ServerInstanceProvider {

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

    String getConfigRef(String instanceName) throws JSONException {
        this.managementResource = this.client.resource(getInstanceUri());
        JSONObject serverResult = getJSONObject(instanceName);
        JSONObject extraProperties = serverResult.getJSONObject("extraProperties");
        JSONObject entity = extraProperties.getJSONObject("entity");
        String result = entity.getString("configRef");
        return result;
    }

    JSONObject getJSONObject(String name) throws UniformInterfaceException {
        JSONObject result = this.managementResource.path(name).accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
        return result;
    }

    String getInstanceUri() {
        return getProtocol() + location.get() + "/management/domain/servers/server/";
    }

    public ServerInstance fetchServerInstanceInfo(String instanceName) {
        authenticator.get().addAuthenticator(client, username.get(), password.get());
        String configRef = null;
        try {
            configRef = getConfigRef(instanceName);

        } catch (Exception e) {
            throw new IllegalStateException("Cannot fetch domain information because of: " + e);
        }
        return new ServerInstance.Builder().name(instanceName).configRef(configRef).build();
    }

    private String getProtocol() {
        String protocol = "http://";
        if (username != null && username.get() != null && !username.get().isEmpty()) {
            protocol = "https://";
        }
        return protocol;
    }
}
