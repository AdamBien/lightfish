package org.lightfish.business.servermonitoring.control.collectors.transactions;

import org.lightfish.business.servermonitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.servermonitoring.control.collectors.DataPoint;
import org.lightfish.business.servermonitoring.control.collectors.SnapshotDataCollector;

/**
 *
 * @author Rob Veldpaus
 */
@SnapshotDataCollector
public class CommitedTransactionsCollector extends AbstractRestDataCollector<Integer> {
    
    public static final String COMMITTED_TX = "transaction-service/committedcount";

    @Override
    public DataPoint<Integer> collect() throws Exception{
        return new DataPoint<>("commitedTransactions",getInt(COMMITTED_TX, "committedcount"));
    }
    
}
