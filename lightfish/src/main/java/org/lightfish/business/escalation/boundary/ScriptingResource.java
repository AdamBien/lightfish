package org.lightfish.business.escalation.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.lightfish.business.escalation.control.Scripting;
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
    Scripting scripting;
    
    @GET
    public List<Script> scripts(){
        return scripting.scripts();
    }

    @GET
    @Path("active")
    public List<Script> activeScripts(){
        return scripting.activeScripts();
    }
    
    @PUT
    public Response save(){
        
        return Response.ok().build();
    }
    
}
