package org.lightfish.presentation.publication.logs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.lightfish.business.servermonitoring.boundary.MonitoringController;
import org.lightfish.business.servermonitoring.control.LogStore;
import org.lightfish.business.servermonitoring.entity.LogRecord;

/**
 *
 * @author rveldpau
 */
@Path("/logs")
@Stateless
@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
public class LogsService {

    private static final Logger LOG = Logger.getLogger(LogsService.class.getName());
    @Inject
    Instance<Integer> defaultMaxLogResults;

    @Inject LogStore logStore;
    
    @GET
    @Path("/for/snapshot/{snapshotId}")
    public List<LogRecord> forSnapshot(
            @PathParam("snapshotId") Long snapshotId
            ){
        LOG.warning("SnapshotId: " + snapshotId);
        
        return logStore.logsForSnapshot(snapshotId);
    }
    
    @GET
    @Path("/for/{instance}/from/{fromDate}{max:(/max/[^/]+?)?}")
    public List<LogRecord> fromDate(
            @PathParam("instance") String instance,
            @PathParam("fromDate") String fromDate,
            @PathParam("max") String maxResults
            ){
        instance = sanitizeInstance(instance);
        LOG.warning("Instance: " + instance);
        LOG.warning("From Date: " + fromDate);
        return logStore.logsFromDate(instance, convertDate(fromDate), parseMaxResults(maxResults));
    }
    
    @GET
    @Path("/for/{instance}/between/{fromDate}/{toDate}{max:(/max/[^/]+?)?}")
    public List<LogRecord> betweenDates(
            @PathParam("instance") String instance,
            @PathParam("fromDate") String fromDate,
            @PathParam("toDate") String toDate,
            @PathParam("max") String maxResults
            ){
        instance = sanitizeInstance(instance);
        LOG.warning("Instance: " + instance);
        LOG.warning("From Date: " + fromDate);
        LOG.warning("To Date: " + toDate);
        LOG.warning("Max: " + parseMaxResults(maxResults));
        return logStore.logsBetweenDates(instance, convertDate(fromDate), convertDate(toDate), parseMaxResults(maxResults));
    }
    
    private String sanitizeInstance(String instance){
        if(instance.isEmpty() || "*".equals(instance)){
            return MonitoringController.COMBINED_SNAPSHOT_NAME;
        }
        return instance;
    }
    
    private Integer parseMaxResults(String maxResultsStr){
        if(maxResultsStr.isEmpty()){
            return defaultMaxLogResults.get();
        }
        Integer value = Integer.valueOf(maxResultsStr.split("/")[2]);
        return value>0?value:null;
    } 
    
    private Date convertDate(String input){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");
        try {
            return format.parse(input);
        } catch (ParseException ex) {
            throw new RuntimeException("Failed to parse supplied date: " + input);
        }
    }

}
