package org.lightview.business.administration.boundary;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;
import junit.framework.Assert;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.lightview.business.methods.boundary.MethodMonitoring;
import org.lightview.presentation.dashboard.DashboardModel;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author: adam-bien.com
 */
public class PollingSetupIT {
    PollingSetup cut;

    @Before
    public void init() {
        this.cut = new PollingSetup();
        this.cut.init();
        this.cut.model = mock(DashboardModel.class);
        StringProperty property = new SimpleStringProperty("http://localhost:8080/lightfish");
        when(this.cut.model.serverUriProperty()).thenReturn(property);
    }

    @Test
    public void setStartStatusAndStop(){
        int EXPECTED_INTERVAL = 42;
        final String status = cut.changeInterval("localhost:4848", EXPECTED_INTERVAL);
        Assert.assertNotNull(status);
        //fetch and compare status
        Pair<String,String> pair = cut.status();
        String interval = pair.getKey();
        String nextTimeout = pair.getValue();
        assertNotNull(nextTimeout);
        assertFalse("-".equals(nextTimeout));
        int actualInterval = Integer.parseInt(interval);
        assertThat(actualInterval, is(EXPECTED_INTERVAL));
        //stop
        final boolean success = cut.stopPolling();
        assertTrue(success);

        pair = cut.status();
        nextTimeout = pair.getValue();
        assertTrue("-".equals(nextTimeout));

    }
}
