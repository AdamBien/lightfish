package org.lightfish.business.monitoring.control.collectors.paranormal;

import org.lightfish.business.monitoring.control.collectors.jvm.*;
import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;

/**
 *
 * @author rveldpau
 */
public class BusyThreadCollector extends AbstractRestDataCollector<Integer> {
    
    public static final String BUSY_THREAD_URI = "network/thread-pool/currentthreadsbusy";

    @Override
    public DataPoint<Integer> collect() throws Exception{
        return new DataPoint<>("busyThreadCount",getInt(BUSY_THREAD_URI, "currentthreadsbusy"));
    }
    
}
