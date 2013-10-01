/*
 *
 */
package org.lightfish.business.appmonitoring.boundary;

import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import org.lightfish.business.appmonitoring.control.EJBStatisticsCollector;
import org.lightfish.business.logging.Log;

/**
 *
 * @author adam-bien.com
 */
@Stateless
public class ApplicationMonitoring {

    @Inject
    EJBStatisticsCollector collector;

    @Inject
    Log LOG;

    public JsonObject getApplicationsContainerStatistics() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonObject applications = collector.fetchApplications();
        Set<Map.Entry<String, JsonValue>> applicationsSet = applications.entrySet();
        for (Map.Entry<String, JsonValue> applicationEntry : applicationsSet) {
            final String applicationName = applicationEntry.getKey();
            JsonObject stats = collector.fetchApplicationStatistics(applicationName);
            builder.add(applicationName, stats);
        }
        return builder.build();
    }

    public JsonObject getApplicationContainerStatistics(String applicationName) {
        return collector.fetchApplicationStatistics(applicationName);
    }

    public JsonObject getBeanStatistics(String applicationName) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonObject components = collector.fetchApplicationComponents(applicationName);
        Set<Map.Entry<String, JsonValue>> entrySet = components.entrySet();
        for (Map.Entry<String, JsonValue> entry : entrySet) {
            String beanName = entry.getKey();
            if (!"server".equals(beanName)) {
                final JsonObject beanStatistics = getBeanStatistics(applicationName, beanName);
                if (beanName == null || beanStatistics == null) {
                    LOG.info("!!!NULL -> Beanname: " + beanName + " -> " + beanStatistics);
                } else {
                    builder.add(beanName, beanStatistics);
                }
            }
        }
        return builder.build();
    }

    public JsonObject getBeanStatistics(String applicationName, String beanName) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonObject components = collector.fetchMethods(applicationName, beanName);
        if (components == null) {
            return builder.addNull("-- no methods --").build();
        }
        Set<Map.Entry<String, JsonValue>> entrySet = components.entrySet();
        for (Map.Entry<String, JsonValue> component : entrySet) {
            String methodName = component.getKey();
            JsonObject methodStatistics = collector.fetchMethodStatistics(applicationName, beanName, methodName);
            if (beanName == null || methodStatistics == null) {
                LOG.info("!!!NULL -> Beanname: " + beanName + " -> " + methodStatistics);
            } else {
                builder.add(methodName, methodStatistics);
            }
        }
        return builder.build();
    }

    public JsonObject getBeanPoolStatistics(String applicationName, String ejbName) {
        return collector.fetchBeanPoolStatistics(applicationName, ejbName);
    }

}
