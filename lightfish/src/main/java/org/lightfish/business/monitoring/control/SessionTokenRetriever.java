package org.lightfish.business.monitoring.control;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import org.lightfish.business.configuration.boundary.Configurator;

/**
 * Retrieves a session token and stores it in the configuration. This is to avoid 
 * the need to re-authenticate with every call.
 * 
 * @author rveldpau
 */
@Singleton
public class SessionTokenRetriever {
    private static final Logger LOG = Logger.getLogger(SessionTokenRetriever.class.getName());
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

    public void retrieveSessionToken() throws UniformInterfaceException {
        authenticator.get().addAuthenticator(client, username.get(), password.get());
        WebResource managementResource = this.client.resource(getSessionsUri());
        JSONObject result = managementResource
                .path("sessions")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Requested-By", "")
                .post(JSONObject.class);
        try {
            JSONObject extraProps = result.getJSONObject("extraProperties");
            String token = extraProps.getString("token");
            configurator.setValue("sessionToken", token);
        } catch (JSONException ex) {
            LOG.log(Level.WARNING, "Failed to authenticate", ex);
        }


    }

    private String getProtocol() {
        String protocol = "http://";
        if (username != null && username.get() != null && !username.get().isEmpty()) {
            protocol = "https://";
        }
        return protocol;
    }
}
