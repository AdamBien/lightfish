package org.lightfish.business.servermonitoring.control.collectors.application;

import java.util.Arrays;
import org.lightfish.business.servermonitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.servermonitoring.control.collectors.DataPoint;
import org.lightfish.business.servermonitoring.entity.Application;

/**
 *
 * @author Rob Veldpaus
 */
@ApplicationDataCollector
public class SpecificApplicationCollector extends AbstractRestDataCollector<Application> {

    private static final String APPLICATIONS = "applications";
    private String applicationName;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public DataPoint<Application> collect() {
        String[] components = getStringArray(APPLICATIONS + "/" + applicationName, "childResources");
        Application app = new Application(applicationName, Arrays.asList(components));
        return new DataPoint<>(getApplicationName(), app);
    }
}
