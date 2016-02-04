package org.lightfish.business.servermonitoring.control.collectors.paranormal;

import java.util.function.BiFunction;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
public class DeadLockedThreadCollector implements BiFunction<RestDataCollector, String, Pair> {

    private static final String DEADLOCKED_THREADS = "jvm/thread-system/deadlockedthreads";

    @Inject
    Logger LOG;

    @Override
    public Pair apply(RestDataCollector collector, String serverInstance) {
        String value = collector.getString(serverInstance, DEADLOCKED_THREADS, "deadlockedthreads", "current");
        return new Pair("deadLockedThreads", value);
    }

}
