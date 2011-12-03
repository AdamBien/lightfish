package org.lightfish.presentation.administration;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import org.lightfish.business.configuration.boundary.Configurator;
import org.lightfish.business.monitoring.boundary.MonitoringController;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Model
public class Index {
    public static final String INTERVAL = "interval";
    public static final String LOCATION = "location";
    
    
    @Inject
    Configurator configurator;

    @Inject
    MonitoringController controller;
    
    @Min(1)
    public int getInterval() {
        return configurator.getValueAsInt(INTERVAL);
    }

    public void setInterval(int interval) {
        this.configurator.setValue(INTERVAL,interval);
    }

    @Size(min=5,max=30)
    public String getLocation() {
        return this.configurator.getValue(LOCATION);
    }

    public void setLocation(String location) {
        this.configurator.setValue(LOCATION,location);
    
    }

    public boolean isRunning(){
        return this.controller.isRunning();
    }
    
    public Object changeAdministration(){
        return null;
    }
    
    public Object start(){
        this.controller.startTimer();
        return null;
    }
    
    public Object stop(){
        this.controller.stopTimer();
        return null;
    }
    
}
