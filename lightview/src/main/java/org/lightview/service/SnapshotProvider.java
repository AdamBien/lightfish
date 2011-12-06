package org.lightview.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.lightview.model.Snapshot;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class SnapshotProvider extends Service<Snapshot> {

    private final String liveDataURL;

    public SnapshotProvider(String liveDataURL) {
        this.liveDataURL = liveDataURL;
    }

    @Override
    protected Task<Snapshot> createTask() {
        return new Task<Snapshot>() {
           
            @Override
            protected Snapshot call() throws Exception {
                SnapshotFetcher fetcher = new SnapshotFetcher(liveDataURL);
                return fetcher.getSnapshot();
            }
        };
    }
}