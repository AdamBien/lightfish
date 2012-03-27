package org.lightview.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.lightview.model.Script;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class ScriptManager {
 
    private String uri;
    private final Client client;

    public ScriptManager(String baseURI) {
        this.uri = withResource(baseURI);
        this.client = Client.create();
    }
    
    
    public List<Script> getAllScripts(){
        GenericType<List<Script>> entities = new GenericType<List<Script>>(){};
        return client.resource(this.uri).accept(MediaType.APPLICATION_XML).get(entities);
    }
    
    public void registerNewScript(Script script){
        client.resource(this.uri).accept(MediaType.APPLICATION_XML).put(script);
    }
    
    public void deleteScript(String scriptName){
        client.resource(this.uri).path(scriptName).delete();
    }

    private String withResource(String baseURI) {
        return baseURI + "/resources/scripts";
    }
}
