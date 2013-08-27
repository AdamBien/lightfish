package org.lightfish.business.servermonitoring.control.collectors;

import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Rob Veldpaus
 */
@Stateless
public class ParallelDataCollectionAction {

    @Inject Logger LOG;
    transient private Exception thrownException = null;
    @Inject
    Instance<Integer> dataCollectionRetries;

    @Asynchronous
    public <TYPE> Future<DataPoint<TYPE>> compute(DataCollector<TYPE> collector) {
        try {
            LOG.log(Level.FINER, "Starting data collection for {0}", collector);
            return new AsyncResult<>(innerCompute(collector, 0));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Data collection on {0} failed, has been retried {1} times.", new Object[]{collector, dataCollectionRetries.get()});
            thrownException = ex;
        }
        return new AsyncResult<>(null);
    }

    private <TYPE> DataPoint<TYPE> innerCompute(DataCollector<TYPE> collector, int attempt) throws Exception {
        try {
            DataPoint<TYPE> dataPoint = collector.collect();
            return dataPoint;
        } catch (Exception ex) {
            if (attempt < dataCollectionRetries.get()) {
                LOG.log(Level.WARNING, "Data collection on {0} failed, retrying...", collector);
                return innerCompute(collector, ++attempt);
            } else {
                throw ex;
            }
        }
    }

    public Exception getThrownException() {
        return thrownException;
    }

}
