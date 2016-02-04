package org.lightfish.business.servermonitoring.control.collectors.transactions;

import java.util.function.BiFunction;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
public class RolledBackTransactionsCollector implements BiFunction<RestDataCollector, String, Pair> {

    public static final String ROLLED_BACK_TX = "transaction-service/rolledbackcount";

    @Override
    public Pair apply(RestDataCollector collector, String serverInstance) {
        return new Pair("rolledBackTransactions", collector.getInt(serverInstance, ROLLED_BACK_TX, "rolledbackcount"));
    }

}
