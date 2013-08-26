package org.lightfish.business.escalation.boundary;

import java.net.URI;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.lightfish.business.escalation.control.ScriptStore;
import org.lightfish.business.escalation.entity.Script;

/**
 * @author adam bien, adam-bien.com
 */
@Path("scripts")
@Stateless
@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
public class ScriptingResource {
 
    @Inject
    ScriptStore scripting;
    
    @Context
    UriInfo uri;
    
    @GET
    public List<Script> scripts(){
        return scripting.scripts();
    }

    @GET
    @Path("{id}")
    public Script script(@PathParam("id") String id){
        return scripting.getScript(id);
    }
    
    @PUT
    public Response save(Script script){
        Script saved = scripting.save(script);
        URI location = uri.getAbsolutePathBuilder().path(saved.getName()).build();
        return Response.created(location).build();
    }

    @DELETE
    @Path("{name}")
    public Response delete(@PathParam("name") String name){
        scripting.delete(name);
        return Response.ok().build();
    }
}
