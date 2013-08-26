package org.lightfish.business.servermonitoring.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import org.lightfish.business.servermonitoring.entity.Domain;

/**
 * User: Rob Veldpaus
 */
public class DomainProvider {

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

    List<String> getInstances() {
        this.managementResource = this.client.target(getInstancesUri());
        JsonObject serverResult = getJsonObject("server");
        JsonObject extraProperties = serverResult.getJsonObject("extraProperties");
        JsonObject result = extraProperties.getJsonObject("childResources");
        Set<Map.Entry<String, JsonValue>> entrySet = result.entrySet();
        List<String> instanceNames = new ArrayList<>();
        for (Map.Entry<String, JsonValue> entry : entrySet) {
            instanceNames.add(entry.getKey());
        }
        return instanceNames;
    }

    JsonObject getJsonObject(String name) {
        JsonObject result = this.managementResource.path(name).request(MediaType.APPLICATION_JSON).get(JsonObject.class);
        return result;
    }

    String getInstancesUri() {
        return getProtocol() + location.get() + "/management/domain/servers";
    }

    public Domain fetchDomainInfo() {
        //TODO migrate authenticator
        // authenticator.get().addAuthenticator(client, username.get(), password.get());
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
