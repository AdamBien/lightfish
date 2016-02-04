package org.lightfish.business.servermonitoring.control.collectors.application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;
import org.lightfish.business.servermonitoring.entity.Application;

/**
 *
 * @author Rob Veldpaus
 */
public class ApplicationCollector implements BiFunction<RestDataCollector, String, Pair> {

    private static final String APPLICATIONS = "applications";
    @Inject
    Logger LOG;

    public Pair apply(RestDataCollector collector, String serverInstance) {
        LOG.fine("Collecting application data");
        String[] applicationNames = applications(collector, serverInstance);
        List<Application> applications = new ArrayList<>();
        for (String appName : applicationNames) {
            applications.add(SpecificApplicationCollector.collect(collector, serverInstance, appName));
        }
        return new Pair("applications", applications);
    }

    private String[] applications(RestDataCollector collector, String serverInstance) {
        return collector.getStringArray(serverInstance, APPLICATIONS, "childResources");
    }

}
