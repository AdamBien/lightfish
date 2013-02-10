package org.lightfish.business.monitoring.control.collectors;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

/**
 *
 * @author adam-bien.com
 */
@Stateless
public class EJBExecutorService {

    @Asynchronous
    public void invoke(ParallelDataCollectionAction dataCollectionAction) {
        dataCollectionAction.compute();
    }
}
