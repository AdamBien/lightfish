/*
 *
 */
package org.lightview.business.pool.boundary;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lightview.business.pool.entity.PoolStatistics;
import org.lightview.presentation.dashboard.DashboardModel;

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
        this.cut.model = mock(DashboardModel.class);
        StringProperty property = new SimpleStringProperty("http://localhost:8080/lightfish");
        when(this.cut.model.serverUriProperty()).thenReturn(property);
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
    public void totalBeansDestroyedForSingleton() {
        PoolStatistics poolStats = this.cut.getPoolStats("lightfish", "Configurator");
        Assert.assertThat(poolStats.getTotalBeansDestroyed(), is(-1));
    }

    @Test
    public void currentThreadsWaitingForSingleton() {
        PoolStatistics poolStats = this.cut.getPoolStats("lightfish", "Configurator");
        Assert.assertThat(poolStats.getCurrentThreadsWaiting(), is(-1));
    }

}
