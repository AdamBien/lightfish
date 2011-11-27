package org.lightfish.business.monitoring.control;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.lightfish.business.monitoring.entity.Snapshot;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class DataProvider {

    public static final String HEAP_SIZE = "jvm/memory/usedheapsize-count";
    private static final String THREAD_COUNT = "jvm/thread-system/threadcount";
    private static final String PEAK_THREAD_COUNT = "jvm/thread-system/peakthreadcount";
    private static final String ERROR_COUNT = "http-service/server/request/errorcount";
    private static final String AVG_PROCESSING_TIME = "http-service/server/request/processingtime";
    private static final String HTTP_BUSY_THREADS = "network/thread-pool/currentthreadsbusy";
    private static final String COMMITTED_TX = "transaction-service/committedcount";
    private static final String ROLLED_BACK_TX = "transaction-service/rolledbackcount";
    private static final String QUEUED_CONNS = "network/connection-queue/countqueued";
    private Client client;
    private String BASE_URL;
    
    @Inject
    String location;

    @PostConstruct
    public void initializeClient() {
        this.client = Client.create();
        this.BASE_URL = "http://"+location+"/monitoring/domain/server/";
        System.out.println("BASE_URL: " + this.BASE_URL);
    }

    public Snapshot fetchData(){
        try {
            long usedHeapSize = usedHeapSize();
            int threadCount = threadCount();
            int peakThreadCount = peakThreadCount();
            int totalErrors = totalErrors();
            int currentThreadBusy = currentThreadBusy();
            int committedTX = committedTX();
            int rolledBackTX = rolledBackTX();
            int queuedConnections = queuedConnections();
            return new Snapshot(usedHeapSize, threadCount,peakThreadCount, totalErrors, currentThreadBusy, committedTX, rolledBackTX, queuedConnections);
        } catch (JSONException e) {
            throw new IllegalStateException("Cannot fetch monitoring data because of: "+ e);
        }
    }

    
    long usedHeapSize() throws JSONException{
        final String uri = BASE_URL + HEAP_SIZE;
        return getLong(uri,"usedheapsize-count");
    }

    int threadCount() throws JSONException{
        final String uri = BASE_URL + THREAD_COUNT;
        return getInt(uri,"threadcount");
    }

    int peakThreadCount() throws JSONException{
        final String uri = BASE_URL + PEAK_THREAD_COUNT;
        return getInt(uri,"peakthreadcount");
    }

    int totalErrors() throws JSONException{
        final String uri = BASE_URL + ERROR_COUNT;
        return getInt(uri,"errorcount");
    }

    int currentThreadBusy() throws JSONException{
        final String uri = BASE_URL + HTTP_BUSY_THREADS;
        return getInt(uri,"currentthreadsbusy");
    }
    
    
    int committedTX() throws JSONException{
        final String uri = BASE_URL + COMMITTED_TX;
        return getInt(uri,"committedcount");
    }

    int rolledBackTX() throws JSONException{
        final String uri = BASE_URL + ROLLED_BACK_TX;
        return getInt(uri,"rolledbackcount");
    }
    
    int queuedConnections() throws JSONException{
        final String uri = BASE_URL + QUEUED_CONNS;
        return getInt(uri,"countqueued");
    }
    
    
    long getLong(String uri,String name) throws JSONException{
        ClientResponse result = client.resource(uri).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        return getJSONObject(result,name).getLong("count");
    
    }
    int getInt(String uri,String name) throws JSONException{
        ClientResponse result = client.resource(uri).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        return getJSONObject(result,name).getInt("count");
    
    }

    JSONObject getJSONObject(ClientResponse result,String name) throws JSONException {
        JSONObject response = result.getEntity(JSONObject.class);
        return response.getJSONObject("extraProperties").
                getJSONObject("entity").
                getJSONObject(name);
    }
}
