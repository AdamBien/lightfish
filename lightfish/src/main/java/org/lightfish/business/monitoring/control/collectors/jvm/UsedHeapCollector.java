package org.lightfish.business.monitoring.control.collectors.jvm;

import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.monitoring.control.collectors.SnapshotDataCollector;

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
