package org.lightfish.business.servermonitoring.control.collectors.jvm;

import java.util.function.BiFunction;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
public class UsedHeapCollector implements BiFunction<RestDataCollector, String, Pair> {

    public static final String HEAP_SIZE = "jvm/memory/usedheapsize-count";

    @Override
    public Pair apply(RestDataCollector collector, String serverInstance) {
        return new Pair("usedHeap", collector.getLong(serverInstance, HEAP_SIZE, "usedheapsize-count"));
    }

}
