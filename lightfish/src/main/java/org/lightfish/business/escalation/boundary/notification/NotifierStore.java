package org.lightfish.business.escalation.boundary.notification;

import org.lightfish.business.escalation.entity.Notifier;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.lightfish.business.escalation.entity.Script;
import org.lightfish.business.monitoring.entity.Snapshot;

/**
 *
 * @author rveldpau
 */
@Stateless
public class NotifierStore {

    @PersistenceContext
    EntityManager em;

    public List<Notifier> all() {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery q = cb.createQuery();
        CriteriaQuery<Notifier> select = q.select(q.from(Notifier.class));
        return this.em.createQuery(select).getResultList();
    }

    public Notifier save(Notifier configuration) {
        return this.em.merge(configuration);
    }

    public void delete(String name) {
        Notifier script = em.getReference(Notifier.class, name);
        em.remove(script);
    }

    public Notifier getNotifier(String id) {
        return em.find(Notifier.class, id);
    }
}
