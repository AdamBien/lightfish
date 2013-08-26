package org.lightfish.business.servermonitoring.control.collectors.web;

import org.lightfish.business.servermonitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.servermonitoring.control.collectors.DataPoint;
import org.lightfish.business.servermonitoring.control.collectors.SnapshotDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
@SnapshotDataCollector
public class ExpiredSessionCollector extends AbstractRestDataCollector<Integer> {
    
    public static final String EXPIRED_SESSIONS_URI = "web/session/expiredsessionstotal";

    @Override
    public DataPoint<Integer> collect() throws Exception{
        return new DataPoint<>("expiredSessionCount",getInt(EXPIRED_SESSIONS_URI, "expiredsessionstotal", "count"));
    }
    
}
