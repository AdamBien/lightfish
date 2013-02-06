package org.lightfish.business.monitoring.control.collectors;

/**
 *
 * @author rveldpau
 */
public interface DataCollector<TYPE> {
    public DataPoint<TYPE> collect() throws Exception;
}
