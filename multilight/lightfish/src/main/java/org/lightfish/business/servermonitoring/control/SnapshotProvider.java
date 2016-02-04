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
package org.lightfish.business.servermonitoring.control;

import java.util.Date;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.lightfish.business.servermonitoring.control.collectors.DataPointToSnapshotMapper;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;
import org.lightfish.business.servermonitoring.entity.Snapshot;

/**
 * @author Adam Bien, blog.adam-bien.com / Rob Veldpaus
 */
public class SnapshotProvider {

    @Inject
    Logger LOG;
    @Inject
    Instance<BiFunction<RestDataCollector, String, Pair>> dataCollectors;
    @Inject
    DataPointToSnapshotMapper mapper;
    @Inject
    Instance<Integer> dataCollectionRetries;

    @Inject
    RestDataCollector collector;

    public Snapshot fetchSnapshot(String instanceName) {
        Date start = new Date();
        Snapshot snapshot = serialDataCollection(instanceName);

        long elapsed = new Date().getTime() - start.getTime();
        LOG.fine("Data collection of " + snapshot + " took " + elapsed);

        return snapshot;

    }

    private Snapshot serialDataCollection(String instanceName) {
        Snapshot snapshot = new Snapshot.Builder().build();
        DataCollectionBehaviour dataCollectionBehaviour = new DataCollectionBehaviour(mapper, snapshot);
        StreamSupport.stream(this.dataCollectors.spliterator(), false).
                map(c -> c.apply(collector, instanceName)).
                map(p -> (Pair) p).
                forEach(dataCollectionBehaviour::perform);
        return snapshot;
    }

    private class DataCollectionBehaviour {

        private DataPointToSnapshotMapper mapper;
        private Snapshot snapshot;

        public DataCollectionBehaviour(DataPointToSnapshotMapper mapper, Snapshot snapshot) {
            this.mapper = mapper;
            this.snapshot = snapshot;
        }

        public void perform(Pair dataPoint) {
            if (dataPoint != null) {
                mapper.mapDataPointToSnapshot(dataPoint, snapshot);
            }
        }
    }
}
