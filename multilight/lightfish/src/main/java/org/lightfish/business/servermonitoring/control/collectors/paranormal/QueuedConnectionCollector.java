package org.lightfish.business.servermonitoring.control.collectors.paranormal;

import java.util.function.BiFunction;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
public class QueuedConnectionCollector implements BiFunction<RestDataCollector, String, Pair> {

    public static final String QUEUED_CONNS = "network/connection-queue/countqueued";

    @Override
    public Pair apply(RestDataCollector collector, String serverInstance) {
        return new Pair("queuedConnectionCount", collector.getInt(serverInstance, QUEUED_CONNS, "countqueued"));
    }

}
