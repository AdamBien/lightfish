package org.lightfish.business.servermonitoring.control;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.lightfish.business.servermonitoring.boundary.MonitoringController;
import org.lightfish.business.servermonitoring.entity.LogRecord;

/**
 *
 * @author rveldpau
 */
public class LogStore {
    @Inject
    Logger LOG;
    
    @PersistenceContext
    EntityManager em;
    
    public List<LogRecord> logsForSnapshot(long snapshotId){
        TypedQuery<LogRecord> query = em.createNamedQuery(LogRecord.QUERY_BY_SNAPSHOT_ID, LogRecord.class);
        query.setParameter("id", snapshotId);
        return query.getResultList();
    }
    
    public List<LogRecord> logsFromDate(String instance, Date fromDate, Integer maxResults){
        TypedQuery<LogRecord> query = em.createNamedQuery(LogRecord.QUERY_FROM, LogRecord.class);
        query.setParameter("instance",handleCombinedInstance(instance));
        query.setParameter("from", fromDate);
        if(maxResults!=null){
            query.setMaxResults(maxResults);
        }
        List<LogRecord> resultList = query.getResultList();
        LOG.warning("Found logs: " + resultList.size());
        return resultList;
    }
    
    public List<LogRecord> logsBetweenDates(String instance, Date fromDate,Date toDate, Integer maxResults){
        TypedQuery<LogRecord> query = em.createNamedQuery(LogRecord.QUERY_BETWEEN, LogRecord.class);
        query.setParameter("instance",handleCombinedInstance(instance));
        query.setParameter("from", fromDate);
        query.setParameter("to", toDate);
        if(maxResults!=null){
            query.setMaxResults(maxResults);
        }
        List<LogRecord> resultList = query.getResultList();
        LOG.warning("Found logs: " + resultList.size());
        return resultList;
    }
    
    private String handleCombinedInstance(String instance){
        if(MonitoringController.COMBINED_SNAPSHOT_NAME.equals(instance)){
            return "%";
        }
        return instance;
    }
}
