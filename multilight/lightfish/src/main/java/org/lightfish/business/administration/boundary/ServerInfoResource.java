/*
 *
 */
package org.lightfish.business.administration.boundary;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
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

    @Context
    ServletContext sc;

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

    @GET
    @Path("lightfish")
    public JsonObject lightfish() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Properties properties = new Properties();
        InputStream is = this.sc.getResourceAsStream("/META-INF/maven/org.glassfish/lightfish/pom.properties");
        if (is != null) {
            try {
                properties.load(is);
            } catch (IOException ex) {
                throw new IllegalStateException("Cannot load properties: " + ex, ex);
            }
        }
        Set<Object> keySet = properties.keySet();
        for (Object object : keySet) {
            builder.add((String) object, (String) properties.get(object));
        }
        return builder.build();
    }
}
