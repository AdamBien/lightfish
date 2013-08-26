package org.lightfish.business.servermonitoring.control.collectors.web;

import org.lightfish.business.servermonitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.servermonitoring.control.collectors.DataPoint;
import org.lightfish.business.servermonitoring.control.collectors.SnapshotDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
@SnapshotDataCollector
public class CurrentSessionCollector extends AbstractRestDataCollector<Integer> {
    
    public static final String SESSION_COUNT_URI = "web/session/activesessionscurrent";

    @Override
    public DataPoint<Integer> collect() throws Exception{
        return new DataPoint<>("sessionCount",getInt(SESSION_COUNT_URI, "activesessionscurrent", "current"));
    }
    
}
