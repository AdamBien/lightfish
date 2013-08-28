package org.lightview.service;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
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
        this.client = ClientBuilder.newClient();
    }

    public List<Script> getAllScripts() {
        GenericType<List<Script>> entities = new GenericType<List<Script>>() {
        };
        try {
            return client.target(this.uri).request(MediaType.APPLICATION_XML).get(entities);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void registerNewScript(Script script) {
        client.target(this.uri).request(MediaType.APPLICATION_XML).put(Entity.xml(script));
    }

    public void deleteScript(String scriptName) {
        client.target(this.uri).path(scriptName).request().delete();
    }

    private String withResource(String baseURI) {
        return baseURI + "/resources/scripts";
    }
}
