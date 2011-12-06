package org.lightview.service;

import com.sun.jersey.api.client.Client;
import javax.ws.rs.core.MediaType;
import org.lightview.model.Snapshot;

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
        final String uri = "http://localhost:8080/lightfish/live";
        for (int i = 0; i < 10; i++) {
            SnapshotFetcher fetcher = new SnapshotFetcher(uri);
            System.out.println("--- " + fetcher.getSnapshot());
        }
    }
}
