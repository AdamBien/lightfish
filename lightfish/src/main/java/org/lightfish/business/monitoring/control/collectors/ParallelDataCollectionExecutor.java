package org.lightfish.business.monitoring.control.collectors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author rveldpau
 */
@Stateless
public class ParallelDataCollectionExecutor {

    private static final Logger LOG = Logger.getLogger(ParallelDataCollectionExecutor.class.getName());
    
    @Inject Instance<ParallelDataCollectionAction> actionInstance;

    public void execute(ParallelDataCollectionActionBehaviour behaviour, Collection<DataCollector> collectors) throws Exception {
        Map<Future<DataPoint>, ParallelDataCollectionAction> futureMap = new HashMap<>(collectors.size());
        
        for (DataCollector collector : collectors) {
            ParallelDataCollectionAction action = actionInstance.get();
            futureMap.put(action.compute(collector), action);
        }

        for (Future<DataPoint> future : futureMap.keySet()) {
            DataPoint dataPoint = future.get();
            if(dataPoint == null){
                ParallelDataCollectionAction action = futureMap.get(future);
                if(action.getThrownException()!=null){
                    throw action.getThrownException();
                }
                continue;
            }
            behaviour.perform(dataPoint);
        }
    }
}
