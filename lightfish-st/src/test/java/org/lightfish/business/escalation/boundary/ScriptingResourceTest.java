package org.lightfish.business.escalation.boundary;

import java.util.List;
import javax.ws.rs.client.Client;
import static javax.ws.rs.client.Entity.entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ClientFactory;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
/**
 *
 * @author adam bien, adam-bien.com
 */
public class ScriptingResourceTest {

    private Client client;
    private String baseURI = "http://localhost:8080/lightfish/resources/scripts";
    private Target target;
            
    @Before
    public void init(){
        this.client = ClientFactory.newClient();
        this.target = this.client.target(baseURI);
    }

    @Test
    public void crudScript(){
        String scriptName = "duke"+System.currentTimeMillis();
        Script script = new Script(scriptName, "true",true);
        //PUT
        Invocation put = this.target.request().buildPut(entity(script,MediaType.APPLICATION_XML));
        Response response = put.invoke();
        assertThat(response.getStatus(),is(201));
        String location = response.getHeaders().getHeader("Location");
        System.out.println("Location: " + location);
        assertTrue(location.endsWith(scriptName));
        
        //GET
        Response fetched = this.client.target(location).request(MediaType.APPLICATION_XML).get();

        //GET (ALL)
        GenericType<List<Script>> list = new GenericType<List<Script>>() {};
        List<Script> result = this.target.request(MediaType.APPLICATION_XML).get(list);
        assertFalse(result.isEmpty());
        
        //DELETE
        response = this.target.path(scriptName).request().delete();
        assertThat(response.getStatus(),is(200));
        
        //GET
        fetched = this.client.target(location).request(MediaType.APPLICATION_XML).get();
        assertThat(fetched.getStatus(),is(204));
    }
    
}
