package org.lightfish.business.servermonitoring.control.collectors.authentication;

import java.util.function.BiFunction;
import java.util.logging.Logger;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import org.lightfish.business.configuration.boundary.Configurator;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;

/**
 *
 * @author rveldpau
 */
public class SessionTokenCollector implements BiFunction<RestDataCollector, String, Pair> {

    @Inject
    Logger LOG;
    @Inject
    Instance<String> username;
    @Inject
    Instance<String> password;
    @Inject
    protected Instance<GlassfishAuthenticator> authenticator;
    @Inject
    Configurator configurator;
    @Inject
    Instance<Boolean> collectLogs;

    @Inject
    Client client;

    public Pair apply(RestDataCollector collector, String serverInstance) {
        String fullUri = collector.getProtocol() + collector.getLocation() + "/management/sessions";
        authenticator.get().addAuthenticator(client, username.get(), password.get());
        LOG.info("Fetching sessions from: " + fullUri);
        Response response = collector.getResponse(fullUri);
        JsonObject result = response.readEntity(JsonObject.class);
        if (result == null) {
            return null;
        }
        JsonObject extraProps = result.getJsonObject("extrapProperties");
        if (extraProps == null) {
            return null;
        }
        String token = extraProps.getString("token");
        if (token == null) {
            return null;
        }
        return new Pair("token", token);
    }

}
