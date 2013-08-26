package org.lightfish.business.servermonitoring.control.collectors;

/**
 *
 * @author Rob Veldpaus
 */
public interface ParallelDataCollectionActionBehaviour<TYPE> {
    void perform(DataPoint<TYPE> dataPoint) throws Exception;
}
