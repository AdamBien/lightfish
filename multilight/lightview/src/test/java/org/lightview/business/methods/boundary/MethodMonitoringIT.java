package org.lightview.business.methods.boundary;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import junit.framework.TestCase;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lightview.business.methods.entity.MethodStatistics;
import org.lightview.business.methods.entity.MethodsStatistics;
import org.lightview.business.pool.boundary.EJBPoolMonitoring;
import org.lightview.presentation.dashboard.DashboardModel;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author: adam-bien.com
 */
public class MethodMonitoringIT {
    MethodMonitoring cut;

    private CountDownLatch blockSupport;
    private MethodsStatistics response;

    @Before
    public void init() {
        this.blockSupport = new CountDownLatch(1);
        this.cut = new MethodMonitoring();
        this.cut.init();
        this.cut.model = mock(DashboardModel.class);
        StringProperty property = new SimpleStringProperty("http://localhost:8080/lightfish");
        when(this.cut.model.serverUriProperty()).thenReturn(property);
    }

    @Test
    public void fetchMethodStatistics() throws InterruptedException {
        this.cut.getMethodStatistics(s -> {
            this.response = s;
            this.blockSupport.countDown();
        }, t -> {
        }, "lightfish", "ConfigurationStore");
        this.blockSupport.await(2000, TimeUnit.MILLISECONDS);
        Assert.assertNotNull(response);
    }

    @Test
    public void extractMethodsFromRequest() throws InterruptedException {
        this.cut.getMethodStatistics(s ->{
            this.response = s;
            this.blockSupport.countDown();
        }, t -> {
            System.err.print(t);
        }, "lightfish", "ConfigurationStore");
        this.blockSupport.await(2000, TimeUnit.MILLISECONDS);
        validateStatistics(response);

    }

    public void validateStatistics(MethodsStatistics methodStatistics) {
        final List<MethodStatistics> all = methodStatistics.all();
        assertFalse(all.isEmpty());
        for (MethodStatistics statistics : all) {
            final String name = statistics.getName();
            assertFalse(name.isEmpty());
            final long invocationCount = statistics.getInvocationCount();
            final long lastExecutionTime = statistics.getLastExecutionTime();
            final long maxTime = statistics.getMaxTime();
            final long totalInvocationTime = statistics.getTotalInvocationTime();
            final long totalNumErrors = statistics.getTotalNumErrors();
            final long totalNumSuccess = statistics.getTotalNumSuccess();
        }
    }

}
