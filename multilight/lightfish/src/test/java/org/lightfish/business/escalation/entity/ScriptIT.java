package org.lightfish.business.escalation.entity;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;

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
        Script active1 = new Script("active1", "true", true);
        Script active2 = new Script("active2", "true", true);
        Script inactive = new Script("active3", "true", false);
        tx.begin();
        em.merge(active1);
        em.merge(active2);
        em.merge(inactive);
        tx.commit();
        tx.begin();
        List<Script> actual = em.createNamedQuery(Script.findAllActive).getResultList();
        assertThat(actual, hasItems(active1, active2));
        assertThat(actual.size(), is(2));
        tx.commit();
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
        assertThat(actual.getContent(), is(expected.getContent()));
        assertThat(actual.getName(), is(expected.getName()));
        tx.commit();
    }
}
