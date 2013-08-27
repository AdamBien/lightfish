/*
 *
 */
package org.lightfish.business.appmonitoring.boundary;

import javax.json.JsonObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lightfish.business.appmonitoring.control.EJBStatisticsCollectorIT;

/**
 *
 * @author adam-bien.com
 */
public class ApplicationMonitoringIT {

    ApplicationMonitoring cut;

    @Before
    public void init() {
        this.cut = new ApplicationMonitoring();
        EJBStatisticsCollectorIT collector = new EJBStatisticsCollectorIT();
        collector.init();
        this.cut.collector = collector.getCut();
    }

    @Test
    public void getApplicationContainerStatisticsForLightfish() {
        JsonObject stats = cut.getApplicationContainerStatistics("lightfish");
        Assert.assertNotNull(stats);
        System.out.println("-- getApplicationContainerStatisticsForLightfish-- " + stats);
    }

    @Test
    public void getApplicationContainerStatistics() {
        JsonObject stats = cut.getApplicationsContainerStatistics();
        Assert.assertNotNull(stats);
        System.out.println("-- getApplicationContainerStatistics-- " + stats);
    }

    @Test
    public void getBeanStatistics() {
        JsonObject stats = cut.getBeanStatistics("lightfish");
        Assert.assertNotNull(stats);
        System.out.println("-- getBeanStatistics-- " + stats);
    }

    @Test
    public void getBeanStatisticsForBean() {
        JsonObject stats = cut.getBeanStatistics("lightfish", "AddScript");
        Assert.assertNotNull(stats);
        System.out.println("-- getBeanStatisticsForBean-- " + stats);
    }

}
