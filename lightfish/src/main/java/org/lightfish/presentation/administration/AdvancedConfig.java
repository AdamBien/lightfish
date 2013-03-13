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
import javax.validation.constraints.Min;
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
    public static final String DATA_COLLECTION_RETRIES = "dataCollectionRetries";
    public static final String COLLECT_LOGS = "collectLogs";
    public static final String PARALLEL_DATA_COLLECTION = "parallelDataCollection";
    public static final String PARALLEL_DATA_COLLECTION_THREADS = "maxParallelThreads";
    @Inject
    Configurator configurator;

    @Inject
    DomainInformation information;

    
    public List<String> getServerList(){
        return this.information.fetch().getInstances();
    }
    
    @NotNull(message="Instance may not be null")
    @Size(min=1, message="Instance must be set")
    public String[] getInstances(){
        return this.configurator.getStringArray(SERVER_INSTANCES);
    }
    
    public void setInstances(String[] instance){
        this.configurator.setArrayValue(SERVER_INSTANCES,instance);
    }
    
    @NotNull @Min(0)
    public Integer getDataCollectionRetries(){
        return Integer.valueOf(this.configurator.getValue(DATA_COLLECTION_RETRIES));
    }
    
    public void setDataCollectionRetries(Integer dataCollectionRetries){
        this.configurator.setValue(DATA_COLLECTION_RETRIES,dataCollectionRetries);
    }
    
    public Boolean getCollectLogs(){
        return Boolean.valueOf(this.configurator.getValue(COLLECT_LOGS));
    }
    
    public void setCollectLogs(Boolean collectLogs){
        this.configurator.setValue(COLLECT_LOGS,collectLogs);
    }
    
    public Boolean getParallelDataCollection(){
        return Boolean.valueOf(this.configurator.getValue(PARALLEL_DATA_COLLECTION));
    }
    
    public void setParallelDataCollection(Boolean parallelDataCollection){
        this.configurator.setValue(PARALLEL_DATA_COLLECTION,parallelDataCollection);
    }
    
    
    @NotNull @Min(0)
    public Integer getParallelDataCollectionThreads(){
        return Integer.valueOf(this.configurator.getValue(PARALLEL_DATA_COLLECTION_THREADS));
    }
    
    public void setParallelDataCollectionThreads(Integer parallelDataCollection){
        this.configurator.setValue(PARALLEL_DATA_COLLECTION_THREADS,parallelDataCollection);
    }
    
    public String updateConfiguration(){
        return "index?faces-redirect=true";
    }
}
