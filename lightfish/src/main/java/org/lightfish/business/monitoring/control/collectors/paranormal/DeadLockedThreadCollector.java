package org.lightfish.business.monitoring.control.collectors.paranormal;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;

/**
 *
 * @author rveldpau
 */
public class DeadLockedThreadCollector extends AbstractRestDataCollector<String> {
    
    
    private static final String DEADLOCKED_THREADS = "jvm/thread-system/deadlockedthreads";
    
    private static final Logger LOG = Logger.getLogger(DeadLockedThreadCollector.class.getName());

    @Override
    public DataPoint<String> collect(){
        String value = null;
        try{
            value = getString(DEADLOCKED_THREADS, "deadlockedthreads", "current");
        }catch(JSONException ex){
            LOG.log(Level.FINE, "Could not find the value for deadlockedthreads, this is not the end of the world.", ex);
        }
        return new DataPoint<>("deadLockedThreads",value);
    }
    
}
