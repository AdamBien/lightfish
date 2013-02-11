package org.lightfish.business.monitoring.control.collectors;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;

/**
 *
 * @author rveldpau
 */
@Singleton
public class DataCollectionForkPoolProvider {

    private static final Logger LOG = Logger.getLogger(DataCollectionForkPoolProvider.class.getName());
    
    private Map<String,ForkJoinPool> pools;
    
    @PostConstruct
    public void initializePoolMap(){
        pools = new HashMap<>(3);
    }
    
    @Produces
    public ForkJoinPool getForkpool(InjectionPoint ip) {
        LOG.finest("Retrieving Pool");
        String className = ip.getMember().getDeclaringClass().getName();
        String memberName = ip.getMember().getName();
        String key = className + "_" + memberName;
        if(!pools.containsKey(key)){
            pools.put(key, new ForkJoinPool());
        }
        
        ForkJoinPool pool = pools.get(key);
        LOG.finest("Pool for " + key + ": " + pool);
        return pool;
    }
}
