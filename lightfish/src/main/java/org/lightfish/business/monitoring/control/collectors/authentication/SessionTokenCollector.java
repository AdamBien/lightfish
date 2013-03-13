package org.lightfish.business.monitoring.control.collectors.authentication;

import com.sun.jersey.api.client.ClientResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import org.lightfish.business.configuration.boundary.Configurator;
import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.monitoring.control.collectors.logs.LogCollector;
import org.lightfish.business.monitoring.entity.LogRecord;

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
        ClientResponse response = getClientResponse("sessions");
        JSONObject result = response.getEntity(JSONObject.class);
        JSONObject extraProps = result.getJSONObject("extrapProperties");
        String token = extraProps.getString("token");
        return new DataPoint<>("token",token);
    }
}
