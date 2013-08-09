package org.lightfish.business.monitoring.control;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import org.lightfish.business.monitoring.entity.ServerInstance;

/**
 * @author Rob Veldpaus
 */
public class ServerInstanceProvider {

    @Inject
    protected Client client;
    @Inject
    Instance<String> location;
    @Inject
    Instance<String> username;
    @Inject
    Instance<String> password;
    @Inject
    Instance<GlassfishAuthenticator> authenticator;
    private WebTarget managementResource;

    String getConfigRef(String instanceName) {
        this.managementResource = this.client.target(getInstanceUri());
        JsonObject serverResult = getJSONObject(instanceName);
        JsonObject extraProperties = serverResult.getJsonObject("extraProperties");
        JsonObject entity = extraProperties.getJsonObject("entity");
        String result = entity.getString("configRef");
        return result;
    }

    JsonObject getJSONObject(String name) {
        JsonObject result = this.managementResource.path(name).request(MediaType.APPLICATION_JSON).get(JsonObject.class);
        return result;
    }

    String getInstanceUri() {
        return getProtocol() + location.get() + "/management/domain/servers/server/";
    }

    public ServerInstance fetchServerInstanceInfo(String instanceName) {
        authenticator.get().addAuthenticator(client, username.get(), password.get());
        String configRef = getConfigRef(instanceName);
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
