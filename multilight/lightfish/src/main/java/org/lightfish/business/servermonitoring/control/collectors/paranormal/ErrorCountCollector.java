package org.lightfish.business.servermonitoring.control.collectors.paranormal;

import java.util.function.BiFunction;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
public class ErrorCountCollector implements BiFunction<RestDataCollector, String, Pair> {

    public static final String ERROR_COUNT_URI = "http-service/server/request/errorcount";

    @Override
    public Pair apply(RestDataCollector collector, String serverInstance) {
        return new Pair("errorCount", collector.getInt(serverInstance, ERROR_COUNT_URI, "errorcount"));
    }

}
