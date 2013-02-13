package org.lightfish.business.escalation.boundary.notification;

import org.lightfish.business.escalation.entity.Notifier;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author rveldpau
 */
@Stateless
public class NotifierStore {

    @PersistenceContext
    EntityManager em;

    public List<Notifier> all(boolean includeSystem) {
        String queryName = includeSystem?"notifier.all":"notifier.nonsystem";
        return em.createNamedQuery(queryName, Notifier.class).getResultList();
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
