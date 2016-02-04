package org.lightfish.business.servermonitoring.control.collectors;

import java.util.Iterator;
import java.util.logging.Logger;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.lightfish.business.servermonitoring.control.SessionTokenRetriever;

/**
 *
 * @author Rob Veldpaus
 */
public class RestDataCollector {

    @Inject
    protected Client client;
    @Inject
    protected Instance<String> location;
    @Inject
    protected Instance<String> sessionToken;
    @Inject
    protected SessionTokenRetriever tokenProvider;

    @Inject
    Logger LOG;

    public long getLong(String serverInstance, String uri, String name) {
        return getLong(serverInstance, uri, name, "count");
    }

    public int getInt(String serverInstance, String uri, String name) {
        return getInt(serverInstance, uri, name, "count");
    }

    public long getLong(String serverInstance, String uri, String name, String key) {
        Response result = getResponse(serverInstance, uri);
        return getJsonObject(result, name).getJsonNumber(key).longValue();
    }

    public int getInt(String serverInstance, String uri, String name, String key) {
        Response result = getResponse(serverInstance, uri);
        return getJsonObject(result, name).getInt(key);
    }

    public String getString(String serverInstance, String uri, String name, String key) {
        Response result = getResponse(serverInstance, uri);
        return getJsonObject(result, name).getString(key);
    }

    public String[] getStringArray(String serverInstance, String name, String key) {
        String[] empty = new String[0];
        Response result = getResponse(serverInstance, name);
        JsonObject response = result.readEntity(JsonObject.class);
        response = response.getJsonObject("extraProperties");
        if (response == null) {
            return empty;
        }
        response = response.getJsonObject("childResources");
        if (response == null) {
            return empty;
        }
        int length = response.size();
        String retVal[] = new String[length];
        Iterator keyIterator = response.keySet().iterator();
        int counter = 0;
        while (keyIterator.hasNext()) {
            retVal[counter++] = (String) keyIterator.next();
        }
        return retVal;
    }

    public JsonObject getJsonObject(Response result, String name) {
        JsonObject response = result.readEntity(JsonObject.class);
        JsonObject extraProperties = response.getJsonObject("extraProperties");
        JsonObject retVal = null;
        if (extraProperties != null) {
            retVal = extraProperties.
                    getJsonObject("entity").
                    getJsonObject(name);
        } else {
            LOG.info("Null retrieved");
        }

        LOG.info("Retrieved JsonObject: " + retVal + " for " + result + " " + name);
        return retVal;
    }

    public String getBaseURI(String serverInstance) {
        return getProtocol() + location.get() + "/monitoring/domain/" + serverInstance + "/";
    }

    public String getProtocol() {
        String protocol = "http://";
        //TODO Error, if username == null, use http anyway
        if (sessionToken != null && sessionToken.get() != null && !sessionToken.get().isEmpty()) {
            protocol = "http://";
        }
        return protocol;
    }

    public String getLocation() {
        return location.get();
    }

    public Response getResponse(String serverInstance, String uri) {
        String fullUri = getBaseURI(serverInstance) + uri;
        return getResponse(fullUri);
    }

    public Response getResponse(String fullUri) {
        WebTarget resource = client.target(fullUri);
        Invocation.Builder builder = resource.request(MediaType.APPLICATION_JSON);
        if (sessionToken != null && sessionToken.get() != null && !sessionToken.get().isEmpty()) {
            builder.cookie(new Cookie("gfresttoken", sessionToken.get()));
        }
        return builder.get(Response.class);

    }

}
