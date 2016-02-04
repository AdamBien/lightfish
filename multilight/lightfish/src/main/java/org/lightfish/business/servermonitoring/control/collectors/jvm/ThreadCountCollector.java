package org.lightfish.business.servermonitoring.control.collectors.jvm;

import java.util.function.BiFunction;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
public class ThreadCountCollector implements BiFunction<RestDataCollector, String, Pair> {

    public static final String THREAD_COUNT_URI = "jvm/thread-system/threadcount";

    @Override
    public Pair apply(RestDataCollector collector, String serverInstance) {
        return new Pair("threadCount", collector.getInt(serverInstance, THREAD_COUNT_URI, "threadcount"));
    }

}
