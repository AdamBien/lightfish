package org.lightview.business.methods.entity;

import javax.json.JsonObject;

/**
 * @author: adam-bien.com
 */
public class MethodStatistics {

    private JsonObject statistics;

    private String name;


    public MethodStatistics(String name,JsonObject statistics) {
        this.statistics = statistics;
        this.name = name;
    }

    public long getLastExecutionTime() {
        return this.statistics.getJsonObject("executionTime").getJsonNumber("count").longValue();
    }

    public long getTotalInvocationTime() {
        return this.statistics.getJsonObject("methodstatistic").getJsonNumber("totalTime").longValue();
    }

    public long getMaxTime() {
        return this.statistics.getJsonObject("methodstatistic").getJsonNumber("maxtime").longValue();
    }

    public long getInvocationCount() {
        return this.statistics.getJsonObject("methodstatistic").getJsonNumber("count").longValue();
    }

    public long getTotalNumErrors() {
        return this.statistics.getJsonObject("totalnumerrors").getJsonNumber("count").longValue();

    }

    public long getTotalNumSuccess() {
        return this.statistics.getJsonObject("totalnumsuccess").getJsonNumber("count").longValue();
    }

    public String getName() {
        return name;
    }
}
