package org.lightfish.business.monitoring.control.collectors;

/**
 *
 * @author Rob Veldpaus
 */
public interface DataCollector<TYPE> {
    public DataPoint<TYPE> collect() throws Exception;
}
