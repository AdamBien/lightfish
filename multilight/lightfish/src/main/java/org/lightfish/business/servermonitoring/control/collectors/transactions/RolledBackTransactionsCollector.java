package org.lightfish.business.servermonitoring.control.collectors.transactions;

import org.lightfish.business.servermonitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.servermonitoring.control.collectors.DataPoint;
import org.lightfish.business.servermonitoring.control.collectors.SnapshotDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
@SnapshotDataCollector
public class RolledBackTransactionsCollector extends AbstractRestDataCollector<Integer> {
    
    public static final String ROLLED_BACK_TX = "transaction-service/rolledbackcount";

    @Override
    public DataPoint<Integer> collect() throws Exception{
        return new DataPoint<>("rolledBackTransactions",getInt(ROLLED_BACK_TX, "rolledbackcount"));
    }
    
}
