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
package org.lightfish.business.monitoring.boundary;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@RequestScoped
public class MonitoringAdmin {
    public static final String OFF = "OFF";
    public static final String ON = "HIGH";
    
    @Inject
    String location;

    private Client client;
    private String baseUri;
    private WebResource resource;
    
    private String modules[] = new String[]{"web-container","ejb-container","thread-pool","jms-service","web-services-container","jpa","transaction-service","jvm","security","jdbc-connection-pool","orb","connector-connection-pool","ejb-container","deployment","connector-service","http-service"};
    
    
    @PostConstruct
    public void initializeClient() {
        this.client = Client.create();
        this.baseUri = "http://"+location+"/management/domain/enable-monitoring";
        this.resource = this.client.resource(this.baseUri);
    }

    
    public boolean activateMonitoring() {
        Form form = new Form();
        form.add("modules", getModulesWithLevel(ON));
        ClientResponse response = this.resource.type("application/x-www-form-urlencoded").post(ClientResponse.class, form);
        return 200 == response.getStatus();
    }

    public boolean deactivateMonitoring() {
        Form form = new Form();
        form.add("modules", getModulesWithLevel(OFF));
        ClientResponse response = this.resource.type("application/x-www-form-urlencoded").post(ClientResponse.class, form);
        return 200 == response.getStatus();
    }
    
    String getModulesWithLevel(String level){
        String retVal = "";
        for (String module : modules) {
            retVal += module + "=" + level + ":";
        }
        return retVal.substring(0,retVal.length()-1);
    }
}
