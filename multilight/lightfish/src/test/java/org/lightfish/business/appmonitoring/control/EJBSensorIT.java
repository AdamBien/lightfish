/*
 *
 */
package org.lightfish.business.appmonitoring.control;

import javax.enterprise.inject.Instance;
import javax.json.JsonObject;
import javax.ws.rs.client.ClientBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

/**
 *
 * @author adam-bien.com
 */
public class EJBSensorIT {

    EJBSensor cut;

    @Before
    public void init() {
        this.cut = new EJBSensor();
        this.cut.location = Mockito.mock(Instance.class);
        this.cut.client = ClientBuilder.newClient();
    }

    @Test
    public void fetch() {
        when(this.cut.location.get()).thenReturn("localhost:4848");
        JsonObject methodStatistics = this.cut.fetch("lightfish", "Configurator", "getString-javax.enterprise.inject.spi.InjectionPoint");
        Assert.assertNotNull(methodStatistics);
        System.out.println("---- " + methodStatistics);
    }

}
