package org.lightfish.business.servermonitoring.control.collectors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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

    @Inject
    Instance<Integer> maxParallelThreads;
    private static final Logger LOG = Logger.getLogger(ParallelDataCollectionExecutor.class.getName());
    @Inject
    Instance<ParallelDataCollectionAction> actionInstance;

    public void execute(ParallelDataCollectionActionBehaviour behaviour, Collection<DataCollector> collectors) throws Exception {
        int maxThreads = maxParallelThreads.get();
        if(maxThreads<=0){
            maxThreads = collectors.size();
        }
        List<DataCollector> remainingCollectors = new ArrayList<>(collectors);
        List<DataCollector> collectorsBeingProcessed = new ArrayList<>(maxThreads);
        
        Map<Future<DataPoint>, ParallelDataCollectionAction> futureMap = new HashMap<>(collectors.size());

        while (!remainingCollectors.isEmpty()) {
            
            for (int i = 0; i < maxThreads && i < remainingCollectors.size(); i++) {
                DataCollector collector = remainingCollectors.get(i);
                ParallelDataCollectionAction action = actionInstance.get();
                futureMap.put(action.compute(collector), action);
                collectorsBeingProcessed.add(collector);
            }

            for (Future<DataPoint> future : futureMap.keySet()) {
                DataPoint dataPoint = future.get();
                if (dataPoint == null) {
                    ParallelDataCollectionAction action = futureMap.get(future);
                    if (action.getThrownException() != null) {
                        throw action.getThrownException();
                    }
                    continue;
                }
                behaviour.perform(dataPoint);
            }
            
            remainingCollectors.removeAll(collectorsBeingProcessed);
            futureMap.clear();
            collectorsBeingProcessed.clear();
        }

    }
}
