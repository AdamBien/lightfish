package org.lightfish.business.servermonitoring.control.collectors.application;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.lightfish.business.servermonitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.servermonitoring.control.collectors.DataPoint;
import org.lightfish.business.servermonitoring.control.collectors.SnapshotDataCollector;
import org.lightfish.business.servermonitoring.entity.Application;

/**
 *
 * @author Rob Veldpaus
 */
@SnapshotDataCollector
public class ApplicationCollector extends AbstractRestDataCollector<List<Application>> {

    private static final String APPLICATIONS = "applications";
    @Inject
    Logger LOG;
    @Inject
    @ApplicationDataCollector
    Instance<SpecificApplicationCollector> specificCollector;
    @Inject
    Instance<Boolean> parallelDataCollection;

    @Override
    public DataPoint<List<Application>> collect() {
        LOG.fine("Collecting application data");
        String[] applicationNames = applications();
        List<SpecificApplicationCollector> collectors = new ArrayList<>(applicationNames.length);
        for (String appName : applicationNames) {
            SpecificApplicationCollector collector = specificCollector.get();
            collector.setApplicationName(appName);
            collector.setServerInstance(getServerInstance());
            collectors.add(collector);
        }
        List<Application> applications = serialRetrieveApplications(collectors);
        return new DataPoint<>("applications", applications);
    }

    private List<Application> serialRetrieveApplications(List<SpecificApplicationCollector> collectors) {
        return collectors.stream().map(c -> c.collect()).map(d -> d.getValue()).collect(Collectors.toList());
    }

    private String[] applications() {
        return getStringArray(APPLICATIONS, "childResources");
    }

}
