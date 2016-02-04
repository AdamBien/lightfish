package org.lightfish.business.servermonitoring.control.collectors.transactions;

import java.util.function.BiFunction;
import org.lightfish.business.servermonitoring.control.collectors.Pair;
import org.lightfish.business.servermonitoring.control.collectors.RestDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
public class CommitedTransactionsCollector implements BiFunction<RestDataCollector, String, Pair> {

    public static final String COMMITTED_TX = "transaction-service/committedcount";

    @Override
    public Pair apply(RestDataCollector collector, String serverInstance) {
        return new Pair("commitedTransactions", collector.getInt(serverInstance, COMMITTED_TX, "committedcount"));
    }

}
