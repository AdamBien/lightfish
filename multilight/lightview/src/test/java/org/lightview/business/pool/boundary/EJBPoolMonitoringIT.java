/*
 *
 */
package org.lightview.business.pool.boundary;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lightview.business.pool.entity.PoolStatistics;

/**
 *
 * @author adam-bien.com
 */
public class EJBPoolMonitoringIT {

    EJBPoolMonitoring cut;

    @Before
    public void init() {
        this.cut = new EJBPoolMonitoring();
        this.cut.init();
    }

    @Test
    public void currentThreadsWaiting() {
        PoolStatistics poolStats = this.cut.getPoolStats("lightfish", "ConfigurationStore");
        Assert.assertNotNull(poolStats.currentThreadsWaiting());
    }

    @Test
    public void threadsWaitingHighwatermark() {
        PoolStatistics poolStats = this.cut.getPoolStats("lightfish", "ConfigurationStore");
        Assert.assertNotNull(poolStats.threadsWaitingHighwatermark());
    }

    @Test
    public void totalBeansCreated() {
        PoolStatistics poolStats = this.cut.getPoolStats("lightfish", "ConfigurationStore");
        Assert.assertNotNull(poolStats.totalBeansCreated());
    }

    @Test
    public void totalBeansDestroyed() {
        PoolStatistics poolStats = this.cut.getPoolStats("lightfish", "ConfigurationStore");
        Assert.assertNotNull(poolStats.totalBeansDestroyed());
    }
}
