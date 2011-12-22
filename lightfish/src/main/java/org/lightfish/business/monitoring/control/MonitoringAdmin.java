package org.lightfish.business.monitoring.control;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
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
