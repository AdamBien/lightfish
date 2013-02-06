package org.lightfish.business.monitoring.control.collectors.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.codehaus.jettison.json.JSONException;
import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.monitoring.control.collectors.ParallelDataCollectionAction;
import org.lightfish.business.monitoring.control.collectors.ParallelDataCollectionActionBehaviour;
import org.lightfish.business.monitoring.control.collectors.SnapshotDataCollector;
import org.lightfish.business.monitoring.entity.ConnectionPool;

/**
 *
 * @author Rob Veldpaus
 */
@SnapshotDataCollector
public class ResourceCollector extends AbstractRestDataCollector<List<ConnectionPool>> {

    private static final String RESOURCES = "resources";
    @Inject
    @ResourceDataCollector
    Instance<SpecificResourceCollector> specificCollector;
    @Inject
    Instance<Boolean> parallelDataCollection;
    @Inject
    ForkJoinPool forkPool;

    @Override
    public DataPoint<List<ConnectionPool>> collect() throws Exception {
        String[] resourceNames = resources();
        List<SpecificResourceCollector> collectors = new ArrayList<>(resourceNames.length);
        for (String jdbcPoolName : resourceNames) {
            SpecificResourceCollector collector = specificCollector.get();
            collector.setResourceName(jdbcPoolName);
            collectors.add(collector);
        }
        
        List<ConnectionPool> resources = null;
        
        if(parallelDataCollection.get()){
            resources = parallelRetrieveResources(collectors);
        }else{
            resources = serialRetrieveResources(collectors);
        }
            

        return new DataPoint<>("resources", resources);
    }

    private List<ConnectionPool> serialRetrieveResources(List<SpecificResourceCollector> collectors) throws Exception {
        List<ConnectionPool> resources = new ArrayList<>(collectors.size());
        for (DataCollector collector : collectors) {
            DataPoint<ConnectionPool> dataPoint = collector.collect();
            resources.add(dataPoint.getValue());
        }
        return resources;
    }

    private List<ConnectionPool> parallelRetrieveResources(List<SpecificResourceCollector> resourceCollectors) throws Exception {
        List<DataCollector> collectors = new ArrayList<>(resourceCollectors.size());
        collectors.addAll(resourceCollectors);
        
        List<ConnectionPool> resources = new ArrayList<>(collectors.size());

        ParallelDataCollectionAction dataCollectionAction =
                new ParallelDataCollectionAction(
                collectors, new DataCollectionBehaviour(resources));
        forkPool.invoke(dataCollectionAction);

        if (dataCollectionAction.getThrownException() != null) {
            throw dataCollectionAction.getThrownException();
        }

        return resources;
    }

    private String[] resources() throws JSONException {
        return getStringArray(RESOURCES, "childResources");
    }

    private class DataCollectionBehaviour implements ParallelDataCollectionActionBehaviour<ConnectionPool> {

        private List<ConnectionPool> resources;

        public DataCollectionBehaviour(List<ConnectionPool> resources) {
            this.resources = resources;
        }

        @Override
        public void perform(DataPoint<ConnectionPool> dataPoint) throws Exception {
            resources.add(dataPoint.getValue());
        }
    }
}
