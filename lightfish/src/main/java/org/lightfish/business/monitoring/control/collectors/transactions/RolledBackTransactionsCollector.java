package org.lightfish.business.monitoring.control.collectors.transactions;

import org.lightfish.business.monitoring.control.collectors.AbstractRestDataCollector;
import org.lightfish.business.monitoring.control.collectors.DataPoint;

/**
 *
 * @author rveldpau
 */
public class RolledBackTransactionsCollector extends AbstractRestDataCollector<Integer> {
    
    public static final String ROLLED_BACK_TX = "transaction-service/rolledbackcount";

    @Override
    public DataPoint<Integer> collect() throws Exception{
        return new DataPoint<>("rolledBackTransactions",getInt(ROLLED_BACK_TX, "rolledbackcount"));
    }
    
}
