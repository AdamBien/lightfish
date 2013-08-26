package org.lightfish.business.escalation.control;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.lightfish.business.escalation.entity.Script;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class ScriptStore {
    
    @PersistenceContext
    EntityManager em;

    public List<Script> scripts() {
        return em.createNamedQuery(Script.findAll).getResultList();
    }

    public List<Script> activeScripts() {
        return em.createNamedQuery(Script.findAllActive).getResultList();
    }
    
    public Script save(Script script){
        return this.em.merge(script);
    }

    public void delete(String name) {
      Script script = em.getReference(Script.class, name);
      em.remove(script);
    }

    public Script getScript(String id) {
        return em.find(Script.class, id);
    }
    
}
