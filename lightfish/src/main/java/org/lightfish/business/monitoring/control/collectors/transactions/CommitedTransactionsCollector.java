package org.lightfish.business.monitoring.control.collectors.transactions;

import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;

/**
 *
 * @author rveldpau
 */
public class CommitedTransactionsCollector extends AbstractRestDataCollector<Integer> {
    
    public static final String COMMITTED_TX = "transaction-service/committedcount";

    @Override
    public DataPoint<Integer> collect() throws Exception{
        return new DataPoint<>("commitedTransactions",getInt(COMMITTED_TX, "committedcount"));
    }
    
}
