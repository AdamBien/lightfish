package org.lightfish.business.monitoring.control.collectors;

import javax.ejb.Asynchronous;

/**
 *
 * @author Rob Veldpaus
 */
public interface DataCollector<TYPE> {
    public DataPoint<TYPE> collect() throws Exception;
}
