package org.lightfish.business.servermonitoring.control.collectors.jvm;

import java.util.function.BiFunction;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
public class PeakThreadCountCollector implements BiFunction<RestDataCollector, String, Pair> {

    public static final String PEAK_THREAD_COUNT_URI = "jvm/thread-system/peakthreadcount";

    @Override
    public Pair apply(RestDataCollector collector, String serverInstance) {
        return new Pair("peakThreadCount", collector.getInt(serverInstance, PEAK_THREAD_COUNT_URI, "peakthreadcount"));
    }

}
