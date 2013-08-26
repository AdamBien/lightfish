package org.lightfish.business.servermonitoring.control;

import java.util.logging.Logger;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import org.lightfish.business.servermonitoring.entity.OneShot;

/**
 * User: blog.adam-bien.com Date: 30.01.12 Time: 19:40
 */
public class OneShotProvider {

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

    private Logger LOG = Logger.getLogger(OneShotProvider.class.getName());

    String getVersion() {
        this.managementResource = this.client.target(getManagementURI());
        JsonObject result = getJsonObject("version");
        return result.getString("message");
    }

    String getUpTime() {
        this.managementResource = this.client.target(getManagementURI());
        JsonObject result = getJsonObject("uptime");
        return result.getString("message");
    }

    JsonObject getJsonObject(String name) {
        return this.managementResource.path(name).request(MediaType.APPLICATION_JSON).get(JsonObject.class);
    }

    String getManagementURI() {
        return getProtocol() + location.get() + "/management/domain/";
    }

    public OneShot fetchOneShot() {
        //TODO migrate authenticator
        //authenticator.get().addAuthenticator(client, username.get(), password.get());
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

    private String getProtocol() {
        String protocol = "http://";
        if (username != null && username.get() != null && !username.get().isEmpty()) {
            protocol = "https://";
            LOG.info("User name is not empty, returning https");
        } else {
            LOG.info("Using " + protocol);
        }
        return protocol;
    }
}
