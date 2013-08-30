/*
 *
 */
package org.lightview.business.pool.boundary;

import static org.hamcrest.CoreMatchers.is;
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
        Assert.assertNotNull(poolStats.currentThreadsWaitingProperty());
    }

    @Test
    public void threadsWaitingHighwatermark() {
        PoolStatistics poolStats = this.cut.getPoolStats("lightfish", "ConfigurationStore");
        Assert.assertNotNull(poolStats.threadsWaitingHighwatermarkProperty());
    }

    @Test
    public void totalBeansCreated() {
        PoolStatistics poolStats = this.cut.getPoolStats("lightfish", "ConfigurationStore");
        Assert.assertNotNull(poolStats.totalBeansCreatedProperty());
    }

    @Test
    public void totalBeansDestroyed() {
        PoolStatistics poolStats = this.cut.getPoolStats("lightfish", "ConfigurationStore");
        Assert.assertNotNull(poolStats.totalBeansDestroyedProperty());
    }

    @Test
    public void totalBeansDestroyedForSingletong() {
        PoolStatistics poolStats = this.cut.getPoolStats("lightfish", "Configurator");
        Assert.assertThat(poolStats.getTotalBeansDestroyed(), is(-1));
    }

}
