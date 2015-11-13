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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import org.lightview.model.Escalation;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class EscalationFetcher {

    private final String url;
    private final Client client;

    public EscalationFetcher(String url) {
        this.url = url;
        this.client = ClientBuilder.newClient();
    }

    public Escalation getEscalation() {
        return client.target(this.url).
                request(MediaType.APPLICATION_XML).
                get(Escalation.class);
    }
}
