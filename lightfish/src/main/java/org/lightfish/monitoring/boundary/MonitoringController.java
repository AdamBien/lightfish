package org.lightfish.monitoring.boundary;

import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.lightfish.monitoring.control.DataProvider;
import org.lightfish.monitoring.entity.Snapshot;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Singleton
@Path("snapshots")
@Produces(MediaType.APPLICATION_JSON)
public class MonitoringController {
    
    @Inject
    DataProvider dataProvider;
    
    @PersistenceContext
    EntityManager em;
    
    @Inject
    Event<Snapshot> escalationSink;
    
    @Schedule(minute="*",second="*/5",hour="*",persistent=false)
    public void gatherAndPersist(){
        Snapshot current = dataProvider.fetchData();
        em.persist(current);
        if(current.isSuspicious())
            escalationSink.fire(current);
    }
    
    @GET
    public List<Snapshot> all(){
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery();
        CriteriaQuery<Snapshot> select = q.select(q.from(Snapshot.class));
        return this.em.createQuery(select).getResultList();
        
    }
}
