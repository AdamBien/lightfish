package org.lightfish.business.servermonitoring.control.collectors.web;

import java.util.function.BiFunction;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
public class CurrentSessionCollector implements BiFunction<RestDataCollector, String, Pair> {

    public static final String SESSION_COUNT_URI = "web/session/activesessionscurrent";

    @Override
    public Pair apply(RestDataCollector collector, String serverInstance) {
        return new Pair("sessionCount", collector.getInt(serverInstance, SESSION_COUNT_URI, "activesessionscurrent", "current"));
    }

}
