package org.lightfish.business.servermonitoring.control.collectors.paranormal;

import org.lightfish.business.servermonitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.servermonitoring.control.collectors.DataPoint;
import org.lightfish.business.servermonitoring.control.collectors.SnapshotDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
@SnapshotDataCollector
public class BusyThreadCollector extends AbstractRestDataCollector<Integer> {

    public static final String BUSY_THREAD_URI = "network/thread-pool/currentthreadsbusy";

    @Override
    public DataPoint<Integer> collect() {
        return new DataPoint<>("busyThreadCount", getInt(BUSY_THREAD_URI, "currentthreadsbusy"));
    }

}
