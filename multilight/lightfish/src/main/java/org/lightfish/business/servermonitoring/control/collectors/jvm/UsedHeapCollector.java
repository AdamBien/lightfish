package org.lightfish.business.servermonitoring.control.collectors.jvm;

import org.lightfish.business.servermonitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.servermonitoring.control.collectors.DataPoint;
import org.lightfish.business.servermonitoring.control.collectors.SnapshotDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
@SnapshotDataCollector
public class UsedHeapCollector extends AbstractRestDataCollector<Long> {
    
    public static final String HEAP_SIZE = "jvm/memory/usedheapsize-count";

    @Override
    public DataPoint<Long> collect() throws Exception{
        return new DataPoint<>("usedHeap",getLong(HEAP_SIZE, "usedheapsize-count"));
    }
    
}
