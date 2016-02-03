package org.lightfish.business.servermonitoring.boundary;

import javax.inject.Inject;
import org.lightfish.business.servermonitoring.control.SnapshotProvider;
import org.lightfish.business.servermonitoring.control.collectors.DataCollector;
import org.lightfish.business.servermonitoring.control.collectors.DataPoint;
import org.lightfish.business.servermonitoring.entity.Snapshot;

/**
 *
 * @author rveldpau
 */
public class SnapshotCollector implements DataCollector<Snapshot> {

    @Inject
    SnapshotProvider dataProvider;

    private String serverInstance;

    @Override
    public String getServerInstance() {
        return this.serverInstance;
    }

    @Override
    public void setServerInstance(String serverInstance) {
        this.serverInstance = serverInstance;
    }

    @Override
    public DataPoint<Snapshot> collect() {
        Snapshot current = dataProvider.fetchSnapshot(getServerInstance());
        current.setInstanceName(getServerInstance());
        return new DataPoint<>(getServerInstance(), current);
    }
}
