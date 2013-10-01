/*
Copyright 2012 Adam Bien, adam-bien.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
                System.out.println("Fetching: " + liveDataURL);
                return fetcher.getSnapshot();
        };
        };
    }
}