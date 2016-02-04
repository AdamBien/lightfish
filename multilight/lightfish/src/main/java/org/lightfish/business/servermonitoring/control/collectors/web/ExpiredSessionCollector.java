package org.lightfish.business.servermonitoring.control.collectors.web;

import java.util.function.BiFunction;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
public class ExpiredSessionCollector implements BiFunction<RestDataCollector, String, Pair> {

    public static final String EXPIRED_SESSIONS_URI = "web/session/expiredsessionstotal";

    @Override
    public Pair apply(RestDataCollector collector, String serverInstance) {
        return new Pair("expiredSessionCount", collector.getInt(serverInstance, EXPIRED_SESSIONS_URI, "expiredsessionstotal", "count"));
    }

}
