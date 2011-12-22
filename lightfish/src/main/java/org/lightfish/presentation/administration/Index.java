package org.lightfish.presentation.administration;

import org.lightfish.business.configuration.boundary.Configurator;
import org.lightfish.business.monitoring.boundary.MonitoringAdmin;
import org.lightfish.business.monitoring.boundary.MonitoringController;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

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

    @Inject
    MonitoringAdmin monitoringAdmin;
    
    @Min(1)
    public int getInterval() {
        return configurator.getValueAsInt(INTERVAL);
    }

    public void setInterval(int interval) {
        this.configurator.setValue(INTERVAL,interval);
    }

    public Object activateMonitoring(){
        this.monitoringAdmin.activateMonitoring();
        return null;
    }

    public Object deactivateMonitoring(){
        this.monitoringAdmin.deactivateMonitoring();
        return null;
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
