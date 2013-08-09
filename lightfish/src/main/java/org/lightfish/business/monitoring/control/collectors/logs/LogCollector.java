package org.lightfish.business.monitoring.control.collectors.logs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import org.lightfish.business.configuration.boundary.Configurator;
import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;
import org.lightfish.business.monitoring.control.collectors.SnapshotDataCollector;
import org.lightfish.business.monitoring.entity.LogRecord;

/**
 *
 * @author Rob Veldpaus
 */
@SnapshotDataCollector
public class LogCollector extends AbstractRestDataCollector<List<LogRecord>> {

    private static final Logger LOG = Logger.getLogger(LogCollector.class.getName());
    @Inject
    Configurator configurator;
    @Inject
    Instance<Boolean> collectLogs;
    public static final String VIEW_LOG_BASE_URI = "view-log/details.json";

    @Override
    protected String getBaseURI() {
        return getProtocol() + getLocation() + "/management/domain/";
    }

    @Override
    public DataPoint<List<LogRecord>> collect() throws Exception {
        if (!collectLogs.get()) {
            return null;
        }

        StringBuilder uriBuilder = new StringBuilder(VIEW_LOG_BASE_URI);
        uriBuilder.append("?instanceName=")
                .append(getServerInstance())
                .append("&fromTime=")
                .append(getLastRunTime() + 1)
                .append("&toTime=")
                .append(System.currentTimeMillis() + 1000)
                .append("&maximumNumberOfResults=1000");
        Response response = getResponse(uriBuilder.toString());
        JsonObject result = response.readEntity(JsonObject.class);
        JsonArray recordsArray = result.getJsonArray("records");
        List<LogRecord> records = new ArrayList<>(recordsArray.size());
        for (int i = 0; i < recordsArray.size(); i++) {
            JsonObject currentRecord = recordsArray.getJsonObject(i);
            String message = currentRecord.getString("Message");
            if (message.length() > 4000) {
                message = message.substring(0, 3996) + "...";
            }
            records.add(
                    new LogRecord.Builder()
                    .instanceName(getServerInstance())
                    .level(currentRecord.getString("loggedLevel"))
                    .loggerName(currentRecord.getString("loggerName"))
                    .message(message)
                    .messageId(currentRecord.getString("messageID", ""))
                    .monitoringTime(new Date(currentRecord.getJsonNumber("loggedDateTimeInMS").longValue()))
                    .nameValuePairs(splitNameValuePairs(currentRecord.getString("nameValuePairs")))
                    .build());
        }
        LOG.log(Level.FINER, "Found {0} log records!", records.size());
        if (!records.isEmpty()) {
            setLastRunTime(records.get(0).getMonitoringTime().getTime());
            LOG.log(Level.FINE, "Last run time is now {0}", getLastRunTime());
        }
        return new DataPoint<>("logs", records);

    }

    private Map<String, String> splitNameValuePairs(String allPairs) {

        String[] splitPairs = allPairs.split(";");

        Map<String, String> pairMap = new HashMap<>(splitPairs.length);
        for (String currentPair : splitPairs) {
            String[] splitPair = currentPair.split("=", 2);
            if (splitPair.length == 2) {
                pairMap.put(splitPair[0], splitPair[1]);
            } else {
                LOG.log(Level.WARNING, "Failed to properly parse {0} for name/value pair map", currentPair);
            }
        }

        return pairMap;

    }

    private long getLastRunTime() {
        String value = configurator.getValue(getLastRunTimeConfigKey());
        if (value == null) {
            return System.currentTimeMillis() - 10000;
        }
        return Long.valueOf(value);
    }

    private void setLastRunTime(long lastRunTime) {
        configurator.setValue(getLastRunTimeConfigKey(), String.valueOf(lastRunTime));
    }

    private String getLastRunTimeConfigKey() {
        return "logs/" + getServerInstance() + "/last";
    }
}
