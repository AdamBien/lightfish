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

import com.sun.jersey.api.client.Client;
import org.lightview.model.Snapshot;

import javax.ws.rs.core.MediaType;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class SnapshotFetcher {

    private final String url;
    private final Client client;

    public SnapshotFetcher(String url) {
        this.url = url;
        this.client = Client.create();
    }

    public Snapshot getSnapshot() {
         return client.resource(this.url).accept(MediaType.APPLICATION_XML).get(Snapshot.class);
    }

    public static void main(String[] args) throws Exception {
        final String uri = "http://localhost:8080/lightfish/live/";
        for (int i = 0; i < 10; i++) {
            SnapshotFetcher fetcher = new SnapshotFetcher(uri);
            System.out.println("--- " + fetcher.getSnapshot());
        }
    }
}
