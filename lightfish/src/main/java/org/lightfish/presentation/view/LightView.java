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
package org.lightfish.presentation.view;

import javax.enterprise.inject.Instance;
import org.lightfish.business.configuration.boundary.Configurator;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import org.lightfish.business.monitoring.control.SnapshotProvider;
import org.lightfish.business.monitoring.entity.Snapshot;

/**
 *
 * @author Rob Veldpaus
 */
@Model
public class LightView {
    public static final String INTERVAL = "interval";

    @Inject
    Configurator configurator;
    
    @Inject Instance<Integer> interval;
    
    @Inject SnapshotProvider snapshotProvider;
    
    public int getInterval() {
        return interval.get();
    }
    
    public Snapshot getSnapshot() throws Exception{
        return snapshotProvider.fetchSnapshot();
    }
    
}
