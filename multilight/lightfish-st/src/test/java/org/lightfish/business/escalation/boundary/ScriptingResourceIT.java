package org.lightfish.business.escalation.boundary;

import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import static javax.ws.rs.client.Entity.entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class ScriptingResourceIT {

    private Client client;
    private String baseURI = "http://localhost:8080/lightfish/resources/scripts";
    private WebTarget target;

    @Before
    public void init() {
        this.client = ClientBuilder.newClient();
        this.target = this.client.target(baseURI);
    }

    @Test
    public void crudScript() {
        String scriptName = "duke" + System.currentTimeMillis();
        Script origin = new Script(scriptName, "true", true);

        //PUT
        Response response = this.target.request().put(entity(origin, MediaType.APPLICATION_XML));
        assertThat(response.getStatus(), is(Status.CREATED.getStatusCode()));
        String location = (String) response.getHeaders().getFirst("Location");
        assertTrue(location.endsWith(scriptName));

        //GET
        Script fetched = this.client.target(location).request(MediaType.APPLICATION_XML).get(Script.class);
        assertThat(fetched, is(origin));

        //GET (ALL)
        GenericType<List<Script>> list = new GenericType<List<Script>>() {
        };
        List<Script> result = this.target.request(MediaType.APPLICATION_XML).get(list);
        assertFalse(result.isEmpty());

        //DELETE
        response = this.target.path(scriptName).request().delete();
        assertThat(response.getStatus(), is(Status.OK.getStatusCode()));

        //GET
        Response notExistingEntity = this.client.target(location).request(MediaType.APPLICATION_XML).get();
        assertThat(notExistingEntity.getStatus(), is(Status.NO_CONTENT.getStatusCode()));
    }

}
