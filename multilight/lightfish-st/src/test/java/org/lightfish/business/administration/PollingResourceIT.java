/*
 *
 */
package org.lightfish.business.administration;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.lightfish.business.RESTSupport;

/**
 *
 * @author adam-bien.com
 */
public class PollingResourceIT extends RESTSupport {

    private String URI = "http://localhost:8080/lightfish/resources/polling";

    @Before
    @Override
    public void init() {
        super.init(URI);
    }

    @Test
    public void startStopPollingWithoutLocation() {
        final int INTERVAL_VALUE = 5;
        //activation
        JsonObject interval = Json.createObjectBuilder().
                add("interval", INTERVAL_VALUE).
                build();
        Response response = super.mainTarget.
                request().
                post(Entity.json(interval));
        Assert.assertThat(response.getStatus(), is(200));
        JsonObject jsonObject = response.readEntity(JsonObject.class);
        String nextTimeout = jsonObject.getString("nextTimeout");
        Assert.assertNotNull(nextTimeout);
        //status
        response = super.mainTarget.request().get();
        Assert.assertThat(response.getStatus(), is(200));
        JsonObject status = response.readEntity(JsonObject.class);
        nextTimeout = status.getString("nextTimeout");
        assertNotNull(nextTimeout);
        String currentInterval = status.getString("interval");
        assertNotNull(currentInterval, is(Integer.parseInt(currentInterval)));
        //stop
        response = super.mainTarget.request().delete();
        Assert.assertThat(response.getStatus(), is(204));

    }

}
