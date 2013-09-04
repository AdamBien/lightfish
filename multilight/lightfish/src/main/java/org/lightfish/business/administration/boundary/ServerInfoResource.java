/*
 *
 */
package org.lightfish.business.administration.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.lightfish.business.servermonitoring.boundary.ServerInformation;
import org.lightfish.business.servermonitoring.entity.OneShot;

/**
 *
 * @author adam-bien.com
 */
@Stateless
@Path("serverinfo")
public class ServerInfoResource {

    @Inject
    ServerInformation information;

    @GET
    public JsonObject info() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        OneShot info = this.information.fetch();
        String version = "--", uptime = "--";
        if (info != null) {
            version = info.getVersion();
        }
        builder.add("version", version);
        if (info != null) {
            uptime = info.getUptime();
        }
        builder.add("uptime", uptime);
        return builder.build();
    }

}
