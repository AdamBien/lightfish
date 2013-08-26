package org.lightfish.business.monitoring.boundary;

import javax.inject.Inject;
import org.lightfish.business.monitoring.control.SnapshotProvider;
import org.lightfish.business.monitoring.control.collectors.DataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.monitoring.entity.Snapshot;

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
        this.serverInstance= serverInstance;
    }

    @Override
    public DataPoint<Snapshot> collect() throws Exception {
        Snapshot current = dataProvider.fetchSnapshot(getServerInstance());
        current.setInstanceName(getServerInstance());
        return new DataPoint<>(getServerInstance(), current);
    }
}
