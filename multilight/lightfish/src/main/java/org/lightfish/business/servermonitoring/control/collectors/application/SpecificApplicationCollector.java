package org.lightfish.business.servermonitoring.control.collectors.application;

import java.util.Arrays;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;
import org.lightfish.business.servermonitoring.entity.Application;

/**
 *
 * @author Rob Veldpaus
 */
public interface SpecificApplicationCollector {

    public static final String APPLICATIONS = "applications";

    public static Application collect(RestDataCollector collector, String serverInstance, String applicationName) {
        String[] components = collector.getStringArray(serverInstance, APPLICATIONS + "/" + applicationName, "childResources");
        return new Application(applicationName, Arrays.asList(components));
    }
}
