package org.lightfish.business.configuration.boundary;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Singleton
public class Configurator {
    private int interval = 5;
    
    private Map<String,String> configuration;
    
    @PostConstruct
    public void initialize(){
        this.configuration = new HashMap<String, String>();
        this.configuration.put("location", "localhost:4848");
        this.configuration.put("jdbcPoolNames","SamplePool");
        this.configuration.put("interval","1");
    }

    @Produces
    public int getInteger(InjectionPoint ip) {
        return Integer.parseInt(getString(ip));
    }

    
    @Produces
    public String getString(InjectionPoint ip) {
        String name = ip.getMember().getName();
        return this.configuration.get(name);
    }
    
    @Produces
    public String[] getStringArray(InjectionPoint ip){
        return asArray(getString(ip));
    }

    public String getValue(String key){
        return this.configuration.get(key);
    }
    
    
    public String[] asArray(String value){
       return value.split(",");
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public int getValueAsInt(String interval) {
        return Integer.parseInt(getValue(interval));
    }

    public void setValue(String key,int interval) {
        this.setValue(key, String.valueOf(interval));
    }

    public void setValue(String key, String value) {
        this.configuration.put(key, value);
    }
}
