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
package org.lightfish.business.monitoring.control;

import com.sun.jersey.api.client.Client;
import java.util.ArrayList;
import java.util.Date;
import org.lightfish.business.monitoring.entity.Snapshot;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import javax.enterprise.inject.Instance;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import java.util.logging.Logger;
import org.lightfish.business.monitoring.control.collectors.DataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.monitoring.control.collectors.DataPointToSnapshotMapper;
import org.lightfish.business.monitoring.control.collectors.ParallelDataCollectionActionBehaviour;
import org.lightfish.business.monitoring.control.collectors.ParallelDataCollectionExecutor;
import org.lightfish.business.monitoring.control.collectors.SnapshotDataCollector;

/**
 * @author Adam Bien, blog.adam-bien.com / Rob Veldpaus
 */
public class SnapshotProvider {

    private static final Logger LOG = Logger.getLogger(SnapshotProvider.class.getName());
    static final String RESOURCES = "resources";
    private Client client;
    @Inject
    Instance<String> location;
    @Inject
    Instance<String> username;
    @Inject
    Instance<String> password;
    @Inject
    Instance<String> serverInstance;
    @Inject
    Instance<Boolean> parallelDataCollection;
    @Inject
    Instance<GlassfishAuthenticator> authenticator;
    @Inject
    @SnapshotDataCollector
    Instance<DataCollector> dataCollectors;
    @Inject
    DataPointToSnapshotMapper mapper;
    @Inject
    ParallelDataCollectionExecutor parallelExecutor;

    @PostConstruct
    public void initializeClient() {
        this.client = Client.create();
    }

    public Snapshot fetchSnapshot() throws Exception {
        authenticator.get().addAuthenticator(client, username.get(), password.get());

        Snapshot snapshot = null;
        Date start = new Date();

        if (parallelDataCollection.get()) {
            snapshot = parallelDataCollection();
        } else {
            snapshot = serialDataCollection();
        }

        long elapsed = new Date().getTime() - start.getTime();
        LOG.fine("Data collection took " + elapsed);

        return snapshot;

    }

    private Snapshot serialDataCollection() throws Exception {
        Snapshot snapshot = new Snapshot.Builder().build();
        for (DataCollector collector : dataCollectors) {
            DataPoint dataPoint = collector.collect();
            mapper.mapDataPointToSnapshot(dataPoint, snapshot);
        }
        return snapshot;
    }

    private Snapshot parallelDataCollection() throws Exception {
        Snapshot snapshot = new Snapshot.Builder().build();
        List<DataCollector> dataCollectorList = new ArrayList<>();
        for (DataCollector collector : dataCollectors) {
            dataCollectorList.add(collector);
        }
        
        parallelExecutor.execute(new DataCollectionBehaviour(mapper, snapshot), dataCollectorList);

        return snapshot;
    }

    private class DataCollectionBehaviour implements ParallelDataCollectionActionBehaviour {

        private DataPointToSnapshotMapper mapper;
        private Snapshot snapshot;

        public DataCollectionBehaviour(DataPointToSnapshotMapper mapper, Snapshot snapshot) {
            this.mapper = mapper;
            this.snapshot = snapshot;
        }

        @Override
        public void perform(DataPoint dataPoint) throws Exception {
            mapper.mapDataPointToSnapshot(dataPoint, snapshot);
        }
    }
}
