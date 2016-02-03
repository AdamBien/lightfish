package org.lightfish.business.servermonitoring.control.collectors.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.lightfish.business.servermonitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.servermonitoring.control.collectors.DataPoint;
import org.lightfish.business.servermonitoring.control.collectors.SnapshotDataCollector;
import org.lightfish.business.servermonitoring.entity.ConnectionPool;

/**
 *
 * @author Rob Veldpaus
 */
@SnapshotDataCollector
public class ResourceCollector extends AbstractRestDataCollector<List<ConnectionPool>> {

    private static final String RESOURCES = "resources";

    @Inject
    @ResourceDataCollector
    Instance<SpecificResourceCollector> specificCollector;

    @Override
    public DataPoint<List<ConnectionPool>> collect() {
        String[] resourceNames = resources();
        List<SpecificResourceCollector> collectors = new ArrayList<>(resourceNames.length);
        for (String jdbcPoolName : resourceNames) {
            SpecificResourceCollector collector = specificCollector.get();
            collector.setResourceName(jdbcPoolName);
            collector.setServerInstance(getServerInstance());
            collectors.add(collector);
        }
        List<ConnectionPool> resources = serialRetrieveResources(collectors);
        return new DataPoint<>("resources", resources);
    }

    private List<ConnectionPool> serialRetrieveResources(List<SpecificResourceCollector> collectors) {
        return collectors.stream().map(c -> c.collect()).map(d -> d.getValue()).collect(Collectors.toList());
    }

    private String[] resources() {
        return getStringArray(RESOURCES, "childResources");
    }
}
