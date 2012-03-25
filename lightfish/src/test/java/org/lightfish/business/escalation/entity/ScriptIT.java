package org.lightfish.business.escalation.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class ScriptIT {
    
    private static EntityManager em;
    private static EntityTransaction tx;

    @BeforeClass
    public static void initalizeEm() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("integration");
        em = emf.createEntityManager();
        tx = em.getTransaction();
    }

    @Test
    public void findActive() {
        
    }

    @Test
    public void persist() {
       Script expected = new Script("optimistic", "true", true);
       tx.begin();
       em.merge(expected);
       tx.commit();
       em.clear();
       tx.begin();
       Script actual = em.find(Script.class, expected.getName());
       assertNotNull(actual);
       assertThat(actual.getContent(),is(expected.getContent()));
       assertThat(actual.getName(),is(expected.getName()));
       tx.commit();
    }
}
