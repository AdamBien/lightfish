package org.lightfish.business.servermonitoring.control.collectors.paranormal;

import java.util.function.BiFunction;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
public class BusyThreadCollector implements BiFunction<RestDataCollector, String, Pair> {

    public static final String BUSY_THREAD_URI = "network/thread-pool/currentthreadsbusy";

    @Override
    public Pair apply(RestDataCollector collector, String serverInstance) {
        return new Pair("busyThreadCount", collector.getInt(serverInstance, BUSY_THREAD_URI, "currentthreadsbusy"));
    }

}
