package org.lightfish.business.servermonitoring.control.collectors.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;
import org.lightfish.business.servermonitoring.entity.ConnectionPool;

/**
 *
 * @author Rob Veldpaus
 */
public class ResourceCollector implements BiFunction<RestDataCollector, String, Pair> {

    private static final String RESOURCES = "resources";

    @Override
    public Pair apply(RestDataCollector collector, String serverInstance) {
        String[] resourceNames = collector.getStringArray(serverInstance, RESOURCES, "childResources");
        List<ConnectionPool> results = new ArrayList<>();
        for (String jdbcPoolName : resourceNames) {
            results.add(SpecificResourceCollector.apply(collector, serverInstance, jdbcPoolName));
        }
        return new Pair("resources", results);
    }
}
