package org.lightfish.business.monitoring.control.collectors;

import java.io.Serializable;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Rob Veldpaus
 */
@Stateless
public class ParallelDataCollectionAction implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(ParallelDataCollectionAction.class.getName());
    transient 
    private Exception thrownException = null;
    
    @Asynchronous
    public <TYPE> Future<DataPoint<TYPE>> compute(DataCollector<TYPE> collector){
        try {
            LOG.finer("Starting data collection for " + collector);
            DataPoint<TYPE> dataPoint = collector.collect();
            return new AsyncResult<>(dataPoint);
        } catch (Exception ex) {
            thrownException = ex;
        }
        return new AsyncResult<>(null);
    }

    public Exception getThrownException() {
        return thrownException;
    }
    
}
