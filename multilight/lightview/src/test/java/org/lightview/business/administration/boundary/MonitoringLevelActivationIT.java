package org.lightview.business.administration.boundary;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
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
public class MonitoringLevelActivationIT {

    MonitoringLevelActivation cut;

    @Before
    public void init() {
        this.cut = new MonitoringLevelActivation();
        this.cut.init();
        this.cut.model = mock(DashboardModel.class);
        StringProperty property = new SimpleStringProperty("http://localhost:8080/lightfish");
        when(this.cut.model.serverUriProperty()).thenReturn(property);
    }

    @Test
    public void activateAndDeactivate(){
        boolean success = this.cut.deactivateMonitoring();
        assertTrue(success);
        success = this.cut.activateMonitoring();
        assertTrue(success);
    }
}
