/*
 *
 */
package org.lightfish.business.administration;

import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lightfish.business.RESTSupport;

/**
 *
 * @author adam-bien.com
 */
public class MonitoringResourceIT extends RESTSupport {

    private String URI = "http://localhost:8080/lightfish/resources/monitoring";

    @Before
    @Override
    public void init() {
        super.init(URI);
    }

    @Test
    public void activateMonitoring() {
        Response response = super.mainTarget.request().post(null);
        Assert.assertThat(response.getStatus(), is(204));
    }

    @Test
    public void deactivateMonitoring() {
        Response response = super.mainTarget.request().delete();
        Assert.assertThat(response.getStatus(), is(204));
    }

}
