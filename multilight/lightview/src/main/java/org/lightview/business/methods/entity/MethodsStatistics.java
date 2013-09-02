package org.lightview.business.methods.entity;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: adam-bien.com
 */
public class MethodsStatistics {

    private final JsonObject statistics;

    public MethodsStatistics(JsonObject statistics) {
            this.statistics = statistics;
    }

    public List<MethodStatistics> all(){
        List<MethodStatistics> statistics = new ArrayList<>();
        final Set<Map.Entry<String,JsonValue>> entries = this.statistics.entrySet();
        for (Map.Entry<String, JsonValue> entry : entries) {
            final String methodName = entry.getKey();
            final JsonObject methodStatistics = (JsonObject) entry.getValue();
            statistics.add(new MethodStatistics(methodName,methodStatistics));
        }
        return statistics;
    }
}
