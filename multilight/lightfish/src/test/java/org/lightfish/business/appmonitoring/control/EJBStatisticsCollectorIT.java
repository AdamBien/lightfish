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
public class EJBStatisticsCollectorIT {

    EJBStatisticsCollector cut;

    @Before
    public void init() {
        this.cut = new EJBStatisticsCollector();
        this.cut.location = Mockito.mock(Instance.class);
        this.cut.client = ClientBuilder.newClient();
        when(this.cut.location.get()).thenReturn("localhost:4848");
    }

    public EJBStatisticsCollector getCut() {
        return cut;
    }

    @Test
    public void fetchMethodStatistics() {
        JsonObject methodStatistics = this.cut.fetchMethodStatistics("lightfish", "Configurator", "getString-javax.enterprise.inject.spi.InjectionPoint");
        Assert.assertNotNull(methodStatistics);
        System.out.println("---- " + methodStatistics);
    }

    @Test
    public void fetchApplicationComponents() {
        JsonObject methodStatistics = this.cut.fetchApplicationComponents("lightfish");
        Assert.assertNotNull(methodStatistics);
        System.out.println("---- " + methodStatistics);
    }

    @Test
    public void fetchApplications() {
        JsonObject methodStatistics = this.cut.fetchApplications();
        Assert.assertNotNull(methodStatistics);
        System.out.println("---- " + methodStatistics);
    }

    @Test
    public void fetchMethods() {
        JsonObject methodsOfBean = this.cut.fetchMethods("lightfish", "Configurator");
        Assert.assertNotNull(methodsOfBean);
        System.out.println(methodsOfBean);
    }

}
