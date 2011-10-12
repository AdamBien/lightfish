package org.lightfish.business.configuration.boundary;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Singleton
public class Configurator {
    private String location = "localhost:4848";
    private int interval = 5;

    @Produces
    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Produces
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    
}
