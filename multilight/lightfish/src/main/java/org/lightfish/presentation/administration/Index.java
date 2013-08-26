/*
Copyright 2012 Adam Bien, adam-bien.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.lightfish.presentation.administration;

import org.lightfish.business.configuration.boundary.Configurator;
import org.lightfish.business.servermonitoring.boundary.MonitoringAdmin;
import org.lightfish.business.servermonitoring.boundary.MonitoringController;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import org.lightfish.business.servermonitoring.boundary.ServerInformation;
import org.lightfish.business.servermonitoring.entity.OneShot;

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
    
    @Inject
    ServerInformation information;
    
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
    
    public String getVersion(){
        OneShot info = this.information.fetch();
        if(info != null){
            return info.getVersion();
        }else{
            return "--";
        }
        
    }

    public String getUptime(){
        OneShot info = this.information.fetch();
        if(info != null){
            return info.getUptime();
        }else{
            return "--";
        }
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
    
    public Object start() throws Exception{
        this.controller.startTimer();
        return null;
    }
    
    public Object stop(){
        this.controller.stopTimer();
        return null;
    }
    
}
