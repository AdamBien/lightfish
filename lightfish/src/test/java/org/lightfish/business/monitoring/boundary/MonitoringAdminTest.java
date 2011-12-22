package org.lightfish.business.monitoring.boundary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class MonitoringAdminTest {
    
    private MonitoringAdmin cut;
    
    @Before
    public void init(){
        this.cut = new MonitoringAdmin();
    }

    @Test
    public void getModulesWithLevel() {
        String expected = "web-container=HIGH:ejb-container=HIGH:";
        String actual = this.cut.getModulesWithLevel("HIGH");
        assertTrue(actual.startsWith(expected));
        assertTrue(actual.endsWith("HIGH"));
        System.out.println(actual);
    }
}
