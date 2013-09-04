/*
 *
 */
package org.lightfish.business.administration;

import javax.json.JsonObject;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.lightfish.business.RESTSupport;

/**
 *
 * @author adam-bien.com
 */
public class ServerInfoResourceIT extends RESTSupport {

    private String URI = "http://localhost:8080/lightfish/resources/serverinfo";

    @Before
    @Override
    public void init() {
        super.init(URI);
    }

    @Test
    public void fetchVersionAndUptime() {
        JsonObject serverInfo = super.mainTarget.request().get(JsonObject.class);
        Assert.assertNotNull(serverInfo);
        String version = serverInfo.getString("version");
        assertNotNull(version);
        String uptime = serverInfo.getString("uptime");
        assertNotNull(uptime);
    }

}
