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
    
    
    @Inject
    Configurator configurator;

    @Inject
    MonitoringController controller;
    
    @Min(1)
    public int getInterval() {
        return configurator.getInterval();
    }

    public void setInterval(int interval) {
        this.configurator.setInterval(interval);
    }

    @Size(min=5,max=30)
    public String getLocation() {
        return this.configurator.getLocation();
    }

    public void setLocation(String location) {
        this.configurator.setLocation(location);
    
    }

    
    public Object changeAdministration(){
        System.out.println(this.configurator.getLocation() + ":" +this.configurator.getInterval());
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
