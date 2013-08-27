package org.lightfish.business.servermonitoring.control;

import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import org.lightfish.business.configuration.boundary.Configurator;

/**
 * Retrieves a session token and stores it in the configuration. This is to
 * avoid the need to re-authenticate with every call.
 *
 * @author rveldpau
 */
@Singleton
public class SessionTokenRetriever {

    @Inject Logger LOG;
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
    @Inject
    Configurator configurator;

    String getSessionsUri() {
        return getProtocol() + location.get() + "/management/";
    }

    public void retrieveSessionToken() {
        //TODO: support authentication with JAX-RS 2.0
        //authenticator.get().addAuthenticator(client, username.get(), password.get());
        WebTarget managementResource = this.client.target(getSessionsUri());
        JsonObject result = managementResource
                .path("sessions")
                .request(MediaType.APPLICATION_JSON)
                .header("X-Requested-By", "")
                .post(Entity.entity(Json.createObjectBuilder().build(), MediaType.APPLICATION_JSON), JsonObject.class);
        JsonObject extraProps = result.getJsonObject("extraProperties");
        String token = extraProps.getString("token");
        configurator.setValue("sessionToken", token);

    }

    private String getProtocol() {
        String protocol = "http://";
        if (username != null && username.get() != null && !username.get().isEmpty()) {
            protocol = "https://";
        }
        return protocol;
    }
}
