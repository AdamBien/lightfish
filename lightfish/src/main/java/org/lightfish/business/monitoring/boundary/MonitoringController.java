package org.lightfish.business.monitoring.boundary;

import org.lightfish.business.monitoring.control.SnapshotProvider;
import org.lightfish.business.monitoring.entity.Snapshot;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */

@Singleton
@Path("snapshots")
@Produces(MediaType.APPLICATION_JSON)
public class MonitoringController {
    
    private Logger LOG = Logger.getLogger(MonitoringController.class.getName());
    
    @Inject
    SnapshotProvider dataProvider;
    
    @PersistenceContext
    EntityManager em;
    
    @Inject @Severity(Severity.Level.ESCALATION)
    Event<Snapshot> escalationSink;

    @Inject @Severity(Severity.Level.HEARTBEAT)
    Event<Snapshot> heartBeat;

    @Resource
    TimerService timerService;

    private Timer timer;
    
    @Inject
    private Instance<Integer> interval;
    
    public void startTimer(){
        ScheduleExpression expression = new ScheduleExpression();
        expression.minute("*").second("*/"+interval.get()).hour("*");
        this.timer = this.timerService.createCalendarTimer(expression);
    }


    @Timeout
    public void gatherAndPersist(){
        Snapshot current = dataProvider.fetchSnapshot();
        em.persist(current);
        heartBeat.fire(current);
        if(current.isSuspicious())
            escalationSink.fire(current);
        LOG.info(".");
    }
    
    @GET
    public List<Snapshot> all(){
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery();
        CriteriaQuery<Snapshot> select = q.select(q.from(Snapshot.class));
        return this.em.createQuery(select).getResultList();
        
    }
    
    @PreDestroy
    public void stopTimer(){
        if(timer != null){
            this.timer.cancel();
            this.timer  = null;
        }
    }

    public boolean isRunning() {
        return (this.timer != null);
    }
}
