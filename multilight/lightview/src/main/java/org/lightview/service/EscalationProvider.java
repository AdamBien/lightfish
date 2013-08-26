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
import org.lightview.model.Escalation;
import org.lightview.model.Snapshot;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class EscalationProvider extends Service<Escalation> {

    private final String liveDataURL;

    public EscalationProvider(String liveDataURL) {
        this.liveDataURL = liveDataURL;
    }

    @Override
    protected Task<Escalation> createTask() {
        return new Task<Escalation>() {
           
            @Override
            protected Escalation call() throws Exception {
                EscalationFetcher fetcher = new EscalationFetcher(liveDataURL);
                System.out.println("Fetching: " + liveDataURL);
                return fetcher.getEscalation();
            }
        };
    }
}