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
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.lightfish.business.authenticator.GlassfishAuthenticator;
import org.lightfish.business.monitoring.control.ServerInstanceProvider;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@RequestScoped
public class MonitoringAdmin {
    public static final String OFF = "OFF";
    public static final String ON = "HIGH";
    
    private static final Logger LOG = Logger.getLogger(MonitoringAdmin.class.getName());
    
    @Inject
    String location;
    @Inject
    String username;
    @Inject
    String password;
    @Inject 
    Instance<String> serverInstance;
    @Inject
    Instance<GlassfishAuthenticator> authenticator;
    @Inject
    ServerInstanceProvider instancePorvider;
    
    private Client client;
    private String baseUri;
    private String modules[] = new String[]{"connectorConnectionPool","connectorService","deployment","ejbContainer","httpService","jdbcConnectionPool","jersey","jmsService","jpa","jvm","orb","security","threadPool","transactionService","webContainer","webServicesContainer"};
    
    
    @PostConstruct
    public void initializeClient() {
        this.client = Client.create();
        this.baseUri = getProtocol() + location;
    }

    
    public boolean activateMonitoring() {
        return changeMonitoringState(ON);
    }


    public boolean deactivateMonitoring() {
        return changeMonitoringState(OFF);
    }

    private boolean changeMonitoringState(String state){
        authenticator.get().addAuthenticator(client, username, password);
        MultivaluedMap formData = new MultivaluedMapImpl();
        for (String module : modules) {
            formData.add(module, state);
        }
        return postForm(formData);
    }
    
    boolean postForm(MultivaluedMap form) throws UniformInterfaceException {
            ClientResponse response = this.client.resource(this.baseUri).path(getEnableMonitoringURI_312()).header("X-Requested-By","LightFish").type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, form);
            int status = response.getStatus();
            LOG.log(Level.INFO, "Got status: {0} for path: {1}  form: {2}", new Object[]{status, getEnableMonitoringURI_312(),form});
            return (200 == response.getStatus());
    }
    
    
    
    private String getProtocol() {
        String protocol = "http://";
        if (username != null && !username.isEmpty()) {
            protocol = "https://";
        }
        return protocol;
    }
    
    private String getEnableMonitoringURI_312(){
        
        String uri = "/management/domain/configs/config/"+
                instancePorvider.fetchServerInstanceInfo().getConfigRef()
                +"/monitoring-service/module-monitoring-levels/";
        return uri;
    }

}
