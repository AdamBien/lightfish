package org.lightfish.business.monitoring.control.collectors;

/**
 *
 * @author Rob Veldpaus
 */
public interface DataCollector<TYPE> {
    String getServerInstance();
    void setServerInstance(String serverInstance);
    public DataPoint<TYPE> collect() throws Exception;
}
