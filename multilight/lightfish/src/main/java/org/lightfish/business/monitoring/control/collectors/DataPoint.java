package org.lightfish.business.monitoring.control.collectors;

/**
 *
 * @author Rob Veldpaus
 */
public class DataPoint<TYPE> {
    private String name;
    private TYPE value;

    public DataPoint(String name, TYPE value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TYPE getValue() {
        return value;
    }

    public void setValue(TYPE value) {
        this.value = value;
    }
    
    
}
