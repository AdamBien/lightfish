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

import java.util.List;
import org.lightfish.business.configuration.boundary.Configurator;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.lightfish.business.monitoring.boundary.DomainInformation;

/**
 *
 * @author Rob Veldpaus
 */
@Model
public class AdvancedConfig {
    public static final String SERVER_INSTANCES = "serverInstances";
    public static final String PARALLEL_DATA_COLLECTION = "parallelDataCollection";
    
    @Inject
    Configurator configurator;

    @Inject
    DomainInformation information;

    
    public List<String> getServerList(){
        return this.information.fetch().getInstances();
    }
    
    @NotNull(message="Instance may not be null")
    @Size(min=1, message="Instance must be set")
    public String getInstance(){
        return this.configurator.getValue(SERVER_INSTANCES);
    }
    
    public void setInstance(String instance){
        this.configurator.setValue(SERVER_INSTANCES,instance);
    }
    
    public Boolean getParallelDataCollection(){
        return Boolean.valueOf(this.configurator.getValue(PARALLEL_DATA_COLLECTION));
    }
    
    public void setParallelDataCollection(Boolean parallelDataCollection){
        this.configurator.setValue(PARALLEL_DATA_COLLECTION,parallelDataCollection);
    }
    
    public String updateConfiguration(){
        return "index?faces-redirect=true";
    }
}
