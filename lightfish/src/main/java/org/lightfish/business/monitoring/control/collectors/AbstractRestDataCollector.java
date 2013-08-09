package org.lightfish.business.monitoring.control.collectors;

import java.util.Iterator;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.lightfish.business.monitoring.control.SessionTokenRetriever;

/**
 *
 * @author Rob Veldpaus
 */
public abstract class AbstractRestDataCollector<TYPE> implements DataCollector<TYPE> {

    @Inject
    protected Client client;
    @Inject
    protected Instance<String> location;
    @Inject
    protected Instance<String> sessionToken;
    @Inject
    protected SessionTokenRetriever tokenProvider;
    private String serverInstance;

    @Override
    public String getServerInstance() {
        return serverInstance;
    }

    @Override
    public void setServerInstance(String serverInstance) {
        this.serverInstance = serverInstance;
    }

    protected long getLong(String uri, String name) {
        return getLong(uri, name, "count");
    }

    protected int getInt(String uri, String name) {
        return getInt(uri, name, "count");
    }

    protected long getLong(String uri, String name, String key) {
        Response result = getResponse(uri);
        return getJsonObject(result, name).getJsonNumber(key).longValue();
    }

    protected int getInt(String uri, String name, String key) {
        Response result = getResponse(uri);
        return getJsonObject(result, name).getInt(key);
    }

    protected String getString(String uri, String name, String key) {
        Response result = getResponse(uri);
        return getJsonObject(result, name).getString(key);
    }

    protected String[] getStringArray(String name, String key) {
        String[] empty = new String[0];
        Response result = getResponse(name);
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

    protected JsonObject getJsonObject(Response result, String name) {
        JsonObject response = result.readEntity(JsonObject.class);
        return response.getJsonObject("extraProperties").
                getJsonObject("entity").
                getJsonObject(name);
    }

    protected String getBaseURI() {
        return getProtocol() + location.get() + "/monitoring/domain/" + serverInstance + "/";
    }

    protected String getProtocol() {
        String protocol = "http://";
        if (sessionToken != null && sessionToken.get() != null && !sessionToken.get().isEmpty()) {
            protocol = "https://";
        }
        return protocol;
    }

    protected String getLocation() {
        return location.get();
    }

    protected Response getResponse(String uri) {
        return getResponse(uri, 0);
    }

    protected Response getResponse(String uri, int retries) {
        String fullUri = getBaseURI() + uri;
        WebTarget resource = client.target(fullUri);
        Invocation.Builder builder = resource.request(MediaType.APPLICATION_JSON);
        if (sessionToken != null && sessionToken.get() != null && !sessionToken.get().isEmpty()) {
            builder.cookie(new Cookie("gfresttoken", sessionToken.get()));
        }
        return builder.get(Response.class);
    }
}
