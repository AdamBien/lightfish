package org.lightfish.business.monitoring.control.collectors;

import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author Rob Veldpaus
 */
public class ParallelDataCollectionAction extends RecursiveAction {

    private ParallelDataCollectionActionBehaviour behaviour;
    private List<DataCollector> dataCollectors;
    private int threshold = 1;
    private Exception thrownException = null;

    public ParallelDataCollectionAction(List<DataCollector> dataCollectors, ParallelDataCollectionActionBehaviour behaviour) {
        this.dataCollectors = dataCollectors;
        this.behaviour = behaviour;
    }

    @Override
    protected void compute() {
        if (this.dataCollectors.size() <= threshold) {
            computeDirectly();
            return;
        }
        int middleIndex = dataCollectors.size() / 2;
        ParallelDataCollectionAction firstHalf =
                new ParallelDataCollectionAction(dataCollectors.subList(0, middleIndex), behaviour);
        ParallelDataCollectionAction secondHalf =
                new ParallelDataCollectionAction(dataCollectors.subList(middleIndex, dataCollectors.size()), behaviour);
        invokeAll(firstHalf, secondHalf);
        if (firstHalf.thrownException != null) {
            thrownException = firstHalf.thrownException;
        }

        if (secondHalf.thrownException != null) {
            thrownException = secondHalf.thrownException;
        }
    }

    private void computeDirectly() {
        try {
            for (DataCollector collector : dataCollectors) {
                DataPoint dataPoint = collector.collect();
                behaviour.perform(dataPoint);
            }
        } catch (Exception ex) {
            thrownException = ex;
        }
    }

    public Exception getThrownException() {
        return thrownException;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
