package org.lightfish.business.monitoring.control.collectors.web;

import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;

/**
 *
 * @author rveldpau
 */
public class ExpiredSessionCollector extends AbstractRestDataCollector<Integer> {
    
    public static final String EXPIRED_SESSIONS_URI = "web/session/expiredsessionstotal";

    @Override
    public DataPoint<Integer> collect() throws Exception{
        return new DataPoint<>("expiredSessionCount",getInt(EXPIRED_SESSIONS_URI, "expiredsessionstotal", "count"));
    }
    
}
