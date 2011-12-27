package org.lightfish.business.monitoring.control;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.lightfish.business.monitoring.entity.ConnectionPool;
import org.lightfish.business.monitoring.entity.Snapshot;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.util.Iterator;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
public class SnapshotProvider {

    public static final String HEAP_SIZE = "jvm/memory/usedheapsize-count";
    private static final String THREAD_COUNT = "jvm/thread-system/threadcount";
    private static final String PEAK_THREAD_COUNT = "jvm/thread-system/peakthreadcount";
    private static final String ERROR_COUNT = "http-service/server/request/errorcount";
    private static final String AVG_PROCESSING_TIME = "http-service/server/request/processingtime";
    private static final String HTTP_BUSY_THREADS = "network/thread-pool/currentthreadsbusy";
    private static final String COMMITTED_TX = "transaction-service/committedcount";
    private static final String ROLLED_BACK_TX = "transaction-service/rolledbackcount";
    private static final String QUEUED_CONNS = "network/connection-queue/countqueued";
    private static final String CURRENT_SESSIONS = "web/session/activesessionscurrent";
    private static final String EXPIRED_SESSIONS = "/web/session/expiredsessionstotal";

    static final String RESOURCES = "resources";


    private Client client;
    private String baseUri;

    @Inject
    String location;

    @PostConstruct
    public void initializeClient() {
        this.client = Client.create();
        this.baseUri = "http://" + location + "/monitoring/domain/server/";
    }

    public Snapshot fetchSnapshot() {
        try {
            long usedHeapSize = usedHeapSize();
            int threadCount = threadCount();
            int peakThreadCount = peakThreadCount();
            int totalErrors = totalErrors();
            int currentThreadBusy = currentThreadBusy();
            int committedTX = committedTX();
            int rolledBackTX = rolledBackTX();
            int queuedConnections = queuedConnections();
            int activeSessionsCurrent = activeSessionsCurrent();
            int expiredSessions = expiredSessions();
            Snapshot snapshot = new Snapshot.Builder().
                    usedHeapSize(usedHeapSize).
                    threadCount(threadCount).
                    peakThreadCount(peakThreadCount).
                    totalErrors(totalErrors).
                    currentThreadBusy(currentThreadBusy).
                    committedTX(committedTX).
                    rolledBackTX(rolledBackTX).
                    queuedConnections(queuedConnections).
                    activeSessions(activeSessionsCurrent).
                    expiredSessions(expiredSessions).
                    build();
            for (String jdbcPoolName : resources()) {
                snapshot.add(fetchResource(jdbcPoolName));
            }
            return snapshot;
        } catch (JSONException e) {
            throw new IllegalStateException("Cannot fetch monitoring data because of: " + e);
        }
    }


    public ConnectionPool fetchResource(String jndiName) {
        try {

            int numconnfree = numconnfree(jndiName);
            int waitqueuelength = waitqueuelength(jndiName);
            int numpotentialconnleak = numpotentialconnleak(jndiName);
            int numconnused = numconnused(jndiName);
            return new ConnectionPool(jndiName, numconnfree, numconnused, waitqueuelength, numpotentialconnleak);
        } catch (JSONException e) {
            throw new IllegalStateException("Cannot fetch monitoring resource data for " + jndiName + " bacause: ", e);
        }
    }

    int numconnfree(String jndiName) throws JSONException {
        String uri = constructResourceString(jndiName);
        return getInt(uri, "numconnfree", "current");
    }

    int numconnused(String jndiName) throws JSONException {
        String uri = constructResourceString(jndiName);
        return getInt(uri, "numconnused", "current");
    }

    int waitqueuelength(String jndiName) throws JSONException {
        String uri = constructResourceString(jndiName);
        return getInt(uri, "waitqueuelength");
    }

    int numpotentialconnleak(String jndiName) throws JSONException {
        String uri = constructResourceString(jndiName);
        return getInt(uri, "numpotentialconnleak");
    }


    String constructResourceString(String resourceName) {
        return baseUri + RESOURCES + "/" + resourceName;
    }

    long usedHeapSize() throws JSONException {
        final String uri = baseUri + HEAP_SIZE;
        return getLong(uri, "usedheapsize-count");
    }

    int threadCount() throws JSONException {
        final String uri = baseUri + THREAD_COUNT;
        return getInt(uri, "threadcount");
    }

    int activeSessionsCurrent() throws JSONException {
        final String uri = baseUri + CURRENT_SESSIONS;
        return getInt(uri, "activesessionscurrent", "current");
    }

    int expiredSessions() throws JSONException {
        final String uri = baseUri + EXPIRED_SESSIONS;
        return getInt(uri, "expiredsessionstotal", "count");
    }


    int peakThreadCount() throws JSONException {
        final String uri = baseUri + PEAK_THREAD_COUNT;
        return getInt(uri, "peakthreadcount");
    }

    int totalErrors() throws JSONException {
        final String uri = baseUri + ERROR_COUNT;
        return getInt(uri, "errorcount");
    }

    int currentThreadBusy() throws JSONException {
        final String uri = baseUri + HTTP_BUSY_THREADS;
        return getInt(uri, "currentthreadsbusy");
    }


    int committedTX() throws JSONException {
        final String uri = baseUri + COMMITTED_TX;
        return getInt(uri, "committedcount");
    }

    int rolledBackTX() throws JSONException {
        final String uri = baseUri + ROLLED_BACK_TX;
        return getInt(uri, "rolledbackcount");
    }

    int queuedConnections() throws JSONException {
        final String uri = baseUri + QUEUED_CONNS;
        return getInt(uri, "countqueued");
    }

    String[] resources() throws JSONException {
        return getStringArray(RESOURCES, "childResources");
    }


    long getLong(String uri, String name) throws JSONException {
        return getLong(uri, name, "count");

    }

    int getInt(String uri, String name) throws JSONException {
        return getInt(uri, name, "count");
    }

    long getLong(String uri, String name, String key) throws JSONException {
        ClientResponse result = client.resource(uri).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        return getJSONObject(result, name).getLong(key);

    }

    int getInt(String uri, String name, String key) throws JSONException {
        ClientResponse result = client.resource(uri).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        return getJSONObject(result, name).getInt(key);
    }

    String[] getStringArray(String name, String key) throws JSONException {
        ClientResponse result = client.resource(this.baseUri + name).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        JSONObject response = result.getEntity(JSONObject.class);
        response = response.getJSONObject("extraProperties").
                getJSONObject("childResources");
        int length = response.length();
        String retVal[] = new String[length];
        Iterator keys = response.keys();
        int counter = 0;
        while (keys.hasNext()) {
            retVal[counter++] = (String) keys.next();
        }
        return retVal;
    }


    JSONObject getJSONObject(ClientResponse result, String name) throws JSONException {
        JSONObject response = result.getEntity(JSONObject.class);
        return response.getJSONObject("extraProperties").
                getJSONObject("entity").
                getJSONObject(name);
    }

}
