package org.lightfish.business.escalation.control;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.lightfish.business.escalation.entity.Script;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class Scripting {
    
    @PersistenceContext
    EntityManager scriptStore;

    public List<Script> scripts() {
        return scriptStore.createNamedQuery(Script.findAll).getResultList();
    }

    public List<Script> activeScripts() {
        return scriptStore.createNamedQuery(Script.findAllActive).getResultList();
    }
    
    public Script save(Script script){
        return this.scriptStore.merge(script);
    }
    
}
