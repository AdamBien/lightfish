package org.lightfish.business.monitoring.control.collectors.authentication;

import java.util.logging.Logger;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import org.lightfish.business.configuration.boundary.Configurator;
import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.monitoring.control.collectors.logs.LogCollector;

/**
 *
 * @author rveldpau
 */
@Authentication
public class SessionTokenCollector extends AbstractRestDataCollector<String> {

    private static final Logger LOG = Logger.getLogger(LogCollector.class.getName());
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

    @Override
    protected String getBaseURI() {
        return getProtocol() + getLocation() + "/management/";
    }

    @Override
    public DataPoint<String> collect() throws Exception {
        authenticator.get().addAuthenticator(client, username.get(), password.get());
        Response response = getResponse("sessions");
        JsonObject result = response.readEntity(JsonObject.class);
        JsonObject extraProps = result.getJsonObject("extrapProperties");
        String token = extraProps.getString("token");
        return new DataPoint<>("token", token);
    }
}
