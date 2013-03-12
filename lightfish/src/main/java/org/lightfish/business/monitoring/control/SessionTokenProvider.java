package org.lightfish.business.monitoring.control;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONObject;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import org.lightfish.business.configuration.boundary.Configurator;
import org.lightfish.business.monitoring.entity.OneShot;

/**
 *
 * @author rveldpau
 */
public class SessionTokenProvider {

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

    @PostConstruct
    public void initializeClient() {
        this.client = Client.create();
    }

    String getSessionsUri() {
        return getProtocol() + location.get() + "/management/";
    }

    public void retrieveSessionToken() throws Exception {
        authenticator.get().addAuthenticator(client, username.get(), password.get());
        WebResource managementResource = this.client.resource(getSessionsUri());
        JSONObject result = managementResource
                .path("sessions")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Requested-By", "")
                .post(JSONObject.class);
        JSONObject extraProps = result.getJSONObject("extraProperties");
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
