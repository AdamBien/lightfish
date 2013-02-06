package org.lightfish.business.monitoring.control.collectors.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Logger;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.codehaus.jettison.json.JSONException;
import org.lightfish.business.monitoring.control.SnapshotProvider;
import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.monitoring.control.collectors.ParallelDataCollectionAction;
import org.lightfish.business.monitoring.control.collectors.ParallelDataCollectionActionBehaviour;
import org.lightfish.business.monitoring.control.collectors.SnapshotDataCollector;
import org.lightfish.business.monitoring.entity.Application;

/**
 *
 * @author Rob Veldpaus
 */
@SnapshotDataCollector
public class ApplicationCollector extends AbstractRestDataCollector<List<Application>> {

    private static final String APPLICATIONS = "applications";
    private static final Logger LOG = Logger.getLogger(SnapshotProvider.class.getName());
    @Inject
    @ApplicationDataCollector
    Instance<SpecificApplicationCollector> specificCollector;
    @Inject
    Instance<Boolean> parallelDataCollection;

    @Override
    public DataPoint<List<Application>> collect() throws Exception {
        LOG.fine("Collecting application data");
        String[] applicationNames = applications();
        LOG.finer("Application names: " + applicationNames.length);
        List<SpecificApplicationCollector> collectors = new ArrayList<>(applicationNames.length);
        for (String appName : applicationNames) {
            SpecificApplicationCollector collector = specificCollector.get();
            collector.setApplicationName(appName);
            collectors.add(collector);
        }
        
        List<Application> applications = null;
        
        if(parallelDataCollection.get()){
            applications = parallelRetrieveApplications(collectors);
        }else{
            applications = serialRetrieveApplications(collectors);
        }
        
        return new DataPoint<>("applications", applications);
    }

    private String[] applications() throws JSONException {
        return getStringArray(APPLICATIONS, "childResources");
    }

    private List<Application> serialRetrieveApplications(List<SpecificApplicationCollector> collectors) throws Exception {
        List<Application> applications = new ArrayList<>(collectors.size());
        for (DataCollector collector : collectors) {
            DataPoint<Application> dataPoint = collector.collect();
            applications.add(dataPoint.getValue());
        }
        return applications;
    }

    private List<Application> parallelRetrieveApplications(List<SpecificApplicationCollector> applicationCollectors) throws Exception {
        List<DataCollector> collectors = new ArrayList<>(applicationCollectors.size());
        collectors.addAll(applicationCollectors);

        List<Application> applications = new ArrayList<>(collectors.size());

        ForkJoinPool forkPool = new ForkJoinPool();
        ParallelDataCollectionAction dataCollectionAction =
                new ParallelDataCollectionAction(
                collectors, new DataCollectionBehaviour(applications));
        forkPool.invoke(dataCollectionAction);

        if (dataCollectionAction.getThrownException() != null) {
            throw dataCollectionAction.getThrownException();
        }

        return applications;
    }

    private class DataCollectionBehaviour implements ParallelDataCollectionActionBehaviour<Application> {

        private List<Application> applications;

        public DataCollectionBehaviour(List<Application> applications) {
            this.applications = applications;
        }

        @Override
        public void perform(DataPoint<Application> dataPoint) throws Exception {
            applications.add(dataPoint.getValue());
        }
    }
}
