package org.lightfish.business.monitoring.control.collectors.logs;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.enterprise.inject.Instance;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import org.lightfish.business.configuration.boundary.Configurator;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.monitoring.entity.LogRecord;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.junit.Assert.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author rveldpau
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(value = WebResource.Builder.class)
public class LogCollectorTest {

    LogCollectorTestWrap instance;
    Client mockClient;
    ClientResponse mockClientResponse;

    @Before
    public void setUp() {
        instance = new LogCollectorTestWrap();
        setupMockClient();
        instance.setClient(mockClient);
        instance.configurator = mock(Configurator.class);
        instance.setAuthenticator(mockInstance(GlassfishAuthenticator.class, mock(GlassfishAuthenticator.class)));
        instance.setLocation(mockInstance(String.class, "myServer"));
        instance.setUsername(mockInstance(String.class, null));
        instance.setPassword(mockInstance(String.class, null));
        instance.collectLogs = mockInstance(Boolean.class, true);
    }

    private void setupMockClient() {
        mockClient = mock(Client.class);
        WebResource mockResource = mock(WebResource.class);
        WebResource.Builder mockBuilder = mock(WebResource.Builder.class);

        when(mockClient.resource(anyString())).thenReturn(mockResource);
        when(mockResource.accept(anyString())).thenReturn(mockBuilder);

        mockClientResponse = mock(ClientResponse.class);
        when(mockBuilder.get(ClientResponse.class)).thenReturn(mockClientResponse);
    }

    @Test
    public void dont_collect() throws Exception {
        instance.collectLogs = mockInstance(Boolean.class, false);

        JSONObject mockResponseObject = mockResponseObject();
        when(mockClientResponse.getEntity(JSONObject.class)).thenReturn(mockResponseObject);

        instance.setServerInstance("test");
        DataPoint<List<LogRecord>> result = instance.collect();

        assertNull(result);
        verify(mockClient, never()).resource(anyString());
    }

    @Test
    public void collect_no_logs() throws Exception {
        JSONObject mockResponseObject = mockResponseObject();
        when(mockClientResponse.getEntity(JSONObject.class)).thenReturn(mockResponseObject);

        instance.setServerInstance("test");
        DataPoint<List<LogRecord>> result = instance.collect();

        assertEquals(0, result.getValue().size());
    }

    @Test
    public void collect_one_log() throws Exception {


        LogRecord[] records = new LogRecord[]{
            new LogRecord.Builder()
            .level("INFO")
            .loggerName("org.lightfish")
            .message("Lightfish is awesome")
            .messageId("LF-AWE")
            .monitoringTime(new Date(123456789l))
            .nameValuePairs(new HashMap<String, String>())
            .build()
        };
        JSONObject mockResponseObject = mockResponseObject(records);
        when(mockClientResponse.getEntity(JSONObject.class)).thenReturn(mockResponseObject);

        instance.setServerInstance("test");
        DataPoint<List<LogRecord>> result = instance.collect();

        assertGoodResults(records, result);
    }

    @Test
    public void collect_one_log_with_name_value_pairs() throws Exception {
        Map<String, String> nameValuePairTest = new HashMap<>();
        nameValuePairTest.put("thread", "109238");
        nameValuePairTest.put("something", "109212");

        LogRecord[] records = new LogRecord[]{
            new LogRecord.Builder()
            .level("INFO")
            .loggerName("org.lightfish")
            .message("Lightfish is awesome")
            .messageId("LF-AWE")
            .monitoringTime(new Date(123456789l))
            .nameValuePairs(nameValuePairTest)
            .build()
        };
        JSONObject mockResponseObject = mockResponseObject(records);
        when(mockClientResponse.getEntity(JSONObject.class)).thenReturn(mockResponseObject);

        instance.setServerInstance("test");
        DataPoint<List<LogRecord>> result = instance.collect();

        assertGoodResults(records, result);
        for (Entry<String, String> entry : nameValuePairTest.entrySet()) {
            assertEquals(entry.getValue(), result.getValue().get(0).getNameValuePairs().get(entry.getKey()));
        }
    }

    @Test
    public void collect_many_logs() throws Exception {

        LogRecord[] records = new LogRecord[]{
            new LogRecord.Builder()
            .level("INFO")
            .loggerName("org.lightfish")
            .message("Lightfish is awesome")
            .messageId("LF-AWE")
            .monitoringTime(new Date(123456789l))
            .nameValuePairs(new HashMap<String, String>())
            .build(),
            new LogRecord.Builder()
            .level("WARNING")
            .loggerName("org.lightfish")
            .message("Lightfish is spectacular")
            .messageId("LF-AWE2")
            .monitoringTime(new Date(123456790l))
            .nameValuePairs(new HashMap<String, String>())
            .build(),
            new LogRecord.Builder()
            .level("SEVER")
            .loggerName("org.lightfish")
            .message("Lightfish is spectacularly amazing!")
            .messageId(null)
            .monitoringTime(new Date(123456791l))
            .nameValuePairs(new HashMap<String, String>())
            .build()
        };
        JSONObject mockResponseObject = mockResponseObject(records);
        when(mockClientResponse.getEntity(JSONObject.class)).thenReturn(mockResponseObject);

        instance.setServerInstance("test");
        DataPoint<List<LogRecord>> result = instance.collect();
        assertGoodResults(records, result);

    }

    private void assertGoodResults(LogRecord[] records, DataPoint<List<LogRecord>> result) {
        assertEquals(records.length, result.getValue().size());
        for (int i = 0; i < records.length; i++) {
            assertEquals(records[0].getLevel(), result.getValue().get(0).getLevel());
            assertEquals(records[0].getLoggerName(), result.getValue().get(0).getLoggerName());
            assertEquals(records[0].getMessage(), result.getValue().get(0).getMessage());
            assertEquals(records[0].getMessageId(), result.getValue().get(0).getMessageId());
            assertEquals(records[0].getMonitoringTime(), result.getValue().get(0).getMonitoringTime());
        }
    }

    private JSONObject mockResponseObject(LogRecord... records) throws Exception {
        JSONObject jsonObj = mock(JSONObject.class);
        JSONArray jsonArray = mock(JSONArray.class);
        when(jsonObj.getJSONArray("records")).thenReturn(jsonArray);

        when(jsonArray.length()).thenReturn(records.length);

        for (int i = 0; i < records.length; i++) {
            JSONObject mockLog = mockJSONLogRecord(records[i]);
            when(jsonArray.getJSONObject(i)).thenReturn(mockLog);
        }

        return jsonObj;
    }

    private JSONObject mockJSONLogRecord(LogRecord record) throws Exception {
        JSONObject logRecord = mock(JSONObject.class);
        when(logRecord.getString("loggedLevel")).thenReturn(record.getLevel());
        when(logRecord.getString("loggerName")).thenReturn(record.getLoggerName());
        when(logRecord.getString("Message")).thenReturn(record.getMessage());
        when(logRecord.optString("messageID")).thenReturn(record.getMessageId());
        when(logRecord.getLong("loggedDateTimeInMS")).thenReturn(record.getMonitoringTime().getTime());

        System.out.println("Time: " + String.valueOf(record.getMonitoringTime().getTime()));

        if (record.getNameValuePairs() != null) {
            StringBuilder nvBuilder = new StringBuilder();
            for (Entry<String, String> entry : record.getNameValuePairs().entrySet()) {
                nvBuilder.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append(";");
            }
            if (nvBuilder.length() > 0) {
                nvBuilder.setLength(nvBuilder.length() - 1);
            }
            when(logRecord.getString("nameValuePairs")).thenReturn(nvBuilder.toString());
        }

        return logRecord;
    }

    private <TYPE> Instance<TYPE> mockInstance(Class<TYPE> type, TYPE value) {
        Instance<TYPE> instance = mock(Instance.class);
        when(instance.get()).thenReturn(value);
        return instance;
    }

    private class LogCollectorTestWrap extends LogCollector {

        public void setLocation(Instance<String> location) {
            this.location = location;
        }

        public void setUsername(Instance<String> username) {
            this.username = username;
        }

        public void setPassword(Instance<String> password) {
            this.password = password;
        }

        public void setAuthenticator(Instance<GlassfishAuthenticator> authenticator) {
            this.authenticator = authenticator;
        }

        void setClient(Client client) {
            this.client = client;
        }
    }
}