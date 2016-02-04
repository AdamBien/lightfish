package org.lightfish.business.servermonitoring.boundary;

import javax.inject.Inject;
import org.lightfish.business.servermonitoring.control.SnapshotProvider;
import org.lightfish.business.servermonitoring.entity.Snapshot;

/**
 *
 * @author rveldpau
 */
public class SnapshotCollector {

    @Inject
    SnapshotProvider dataProvider;

    public Snapshot collect(String serverInstance) {
        Snapshot current = dataProvider.fetchSnapshot(serverInstance);
        current.setInstanceName(serverInstance);
        return current;
    }
}
